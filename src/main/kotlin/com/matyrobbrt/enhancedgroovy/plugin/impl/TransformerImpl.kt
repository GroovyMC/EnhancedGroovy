package com.matyrobbrt.enhancedgroovy.plugin.impl

import com.intellij.psi.PsiAnnotation
import com.matyrobbrt.enhancedgroovy.dsl.ClassTransformer
import com.matyrobbrt.enhancedgroovy.plugin.util.GroovyLightField
import org.jetbrains.plugins.groovy.lang.groovydoc.psi.impl.GrDocCommentImpl
import org.jetbrains.plugins.groovy.transformations.TransformationContext

class TransformerImpl(private val context: TransformationContext, var currentAnnotation: PsiAnnotation?) : ClassTransformer {

    override fun addField(options: MutableMap<String, Any>) {
        val field = GroovyLightField(options["name"].toString(), options["type"].toString(), context.codeClass)
        field.setContainingClass(context.codeClass)
        val annotation = currentAnnotation!!
        field.navigationElement = annotation
        field.originInfo = "created by @${annotation.qualifiedName}"
        (options.getOrDefault("modifiers", listOf<String>()) as List<*>).forEach { field.modifierList.addModifier(it.toString()) }
        val doc = options["doc"]
        if (doc != null) {
            field.docs = GrDocCommentImpl(doc.toString())
        }
        context.fields.add(field)
    }

    override fun addField(name: String, type: String) {
        this.addField(mutableMapOf(
            "name" to name,
            "type" to type
        ))
    }

    override fun getFields(): List<ReadOnlyField> = context.fields.map { ReadOnlyField(it) }

    override fun getClassName() = context.className
}