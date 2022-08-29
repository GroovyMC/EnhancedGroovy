package com.matyrobbrt.enhancedgroovy.plugin

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.ProjectScope
import com.intellij.util.CommonProcessors
import com.intellij.util.indexing.FileBasedIndex
import com.matyrobbrt.enhancedgroovy.dsl.GroovyAnnotation
import com.matyrobbrt.enhancedgroovy.plugin.impl.TransformerImpl
import com.matyrobbrt.enhancedgroovy.plugin.index.IDECompatibilityData
import com.matyrobbrt.enhancedgroovy.plugin.index.IDESupportScriptIndexer
import groovy.lang.Binding
import groovy.lang.GroovyShell
import groovy.lang.Script
import org.jetbrains.plugins.groovy.transformations.AstTransformationSupport
import org.jetbrains.plugins.groovy.transformations.TransformationContext

class IDECompatASTSupport : AstTransformationSupport {
    companion object {
        private val shell = GroovyShell(IDECompatASTSupport::class.java.classLoader)
        // TODO clear map regularly
        private val scriptCache: java.util.HashMap<VirtualFile, WithBytes> = java.util.LinkedHashMap()

        private fun createScript(file: VirtualFile): Script {
            return shell.parse(String(file.contentsToByteArray()))
        }

        @Synchronized fun getScript(file: VirtualFile, immutable: Boolean): Script {
            synchronized(scriptCache) {
                if (!immutable) {
                    if (true) {
                        println("Immutable: $file")
                        return createScript(file)
                    }
                    // TODO it seems like it doesn't like the current approach?
                    val bytes = file.contentsToByteArray()
                    val existing: WithBytes? = scriptCache[file]
                    if (existing != null && existing.bytes.contentEquals(bytes)) {
                        return existing.script
                    }

                    val new = shell.parse(String(bytes))
                    scriptCache[file] = WithBytes(new, bytes)
                    return new
                }
                return scriptCache.computeIfAbsent(file) { WithBytes(createScript(it), byteArrayOf()) }.script
            }
        }

        fun createTransformer(context: TransformationContext): TransformerImpl {
            return TransformerImpl(context, null)
        }

        data class WithBytes(val script: Script, val bytes: ByteArray) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as WithBytes

                if (script != other.script) return false
                if (!bytes.contentEquals(other.bytes)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = script.hashCode()
                result = 31 * result + bytes.contentHashCode()
                return result
            }
        }
    }

    override fun applyTransformation(context: TransformationContext) {
        val index = FileBasedIndex.getInstance()
        val transformer = createTransformer(context)
        context.codeClass.annotations.forEach {
            transformer.currentAnnotation = it

            // TODO it really doesn't like inside-project scripts
            listOf(true).forEach { type ->
                val qualified = it.resolveAnnotationType()?.qualifiedName ?: return
                val collector = CommonProcessors.CollectProcessor<VirtualFile>(ArrayList())
                index.getFilesWithKey(
                    IDESupportScriptIndexer.name,
                    setOf(IDECompatibilityData(qualified, type)),
                    collector,
                    ProjectScope.getLibrariesScope(context.project)
                )
                val annotationData = GroovyAnnotation()
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