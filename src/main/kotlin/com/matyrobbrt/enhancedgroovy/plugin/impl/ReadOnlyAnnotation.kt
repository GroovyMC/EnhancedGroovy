package com.matyrobbrt.enhancedgroovy.plugin.impl

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiAnnotationMemberValue
import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiLiteral
import com.matyrobbrt.enhancedgroovy.dsl.members.Annotation

internal class ReadOnlyAnnotation(private val annotation: PsiAnnotation) :
    Annotation {
    override fun getType(): String {
        return annotation.qualifiedName!!
    }

    override fun getAttribute(name: String): Any? {
        return resolveAttribute(annotation.findAttributeValue(name) ?: return null)
    }

    private fun resolveAttribute(value: PsiAnnotationMemberValue): Any? {
        return when (value) {
            is PsiLiteral -> value.value
            is PsiArrayInitializerMemberValue -> value.initializers.map { resolveAttribute(it) }
            else -> value
        }
    }
}