package com.matyrobbrt.enhancedgroovy.plugin

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.CommonProcessors
import com.intellij.util.indexing.FileBasedIndex
import com.matyrobbrt.enhancedgroovy.plugin.impl.ReadOnlyAnnotation
import com.matyrobbrt.enhancedgroovy.plugin.impl.TransformerImpl
import com.matyrobbrt.enhancedgroovy.plugin.index.IDECompatibilityData
import com.matyrobbrt.enhancedgroovy.plugin.index.IDESupportScriptIndexer
import groovy.lang.Binding
import groovy.lang.GroovyShell
import groovy.lang.Script
import org.jetbrains.plugins.groovy.transformations.AstTransformationSupport
import org.jetbrains.plugins.groovy.transformations.TransformationContext
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class IDECompatASTSupport : AstTransformationSupport {
    companion object {
        private val shell: ThreadLocal<GroovyShell> = ThreadLocal.withInitial { GroovyShell(IDECompatASTSupport::class.java.classLoader) }
        private val scriptCache: Cache<VirtualFile, Script> = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build()

        private fun createScript(file: VirtualFile): Script {
            return shell.get().parse(String(file.contentsToByteArray()))
        }

        @Synchronized fun getScript(file: VirtualFile, immutable: Boolean): Script {
            if (!immutable) {
                return createScript(file)
            }
            return scriptCache.get(file) { createScript(file) }
        }

        fun createTransformer(context: TransformationContext): TransformerImpl {
            return TransformerImpl(context, null)
        }
    }

    override fun applyTransformation(context: TransformationContext) {
        context.codeClass
        val index = FileBasedIndex.getInstance()
        val transformer = createTransformer(context)
        val module = ModuleUtil.findModuleForFile(context.codeClass.containingFile)
        val scope: GlobalSearchScope = if (module == null) GlobalSearchScope.allScope(context.project)
            else GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)
        context.codeClass.annotations.forEach {
            transformer.currentAnnotation = it

            // TODO in-project still doesn't work properly... yay deadlocks
            listOf(true).forEach { type ->
                val qualified = it.resolveAnnotationType()?.qualifiedName ?: return
                val collector = CommonProcessors.CollectProcessor<VirtualFile>(ArrayList())
                index.getFilesWithKey(
                    IDESupportScriptIndexer.name,
                    setOf(IDECompatibilityData(qualified, type)),
                    collector,
                    scope
                )
                val annotationData = ReadOnlyAnnotation(it)
                collector.results.forEach { virtualFile ->
                    val script = getScript(virtualFile, type)
                    script.binding = Binding(mutableMapOf(
                        "transformer" to transformer,
                        "annotation" to annotationData
                    ))
                    script.run()
                }
            }
        }
    }
}