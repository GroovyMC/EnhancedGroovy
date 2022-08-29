package com.matyrobbrt.enhancedgroovy.plugin.impl

import com.intellij.psi.PsiAnnotation
import com.matyrobbrt.enhancedgroovy.dsl.ClassTransformer
import org.jetbrains.plugins.groovy.lang.psi.impl.synthetic.GrLightField
import org.jetbrains.plugins.groovy.transformations.TransformationContext

class TransformerImpl(private val context: TransformationContext, var currentAnnotation: PsiAnnotation?) : ClassTransformer {
    override fun addMethod(name: String, type: String) {
        TODO("Not yet implemented")
    }

    override fun addField(name: String, type: String) {
        context.addField(GrLightField(name, type, context.codeClass).apply {
            navigationElement = currentAnnotation ?: return
            modifierList.addModifier("public")
            modifierList.addModifier("static")
        })
    }
}