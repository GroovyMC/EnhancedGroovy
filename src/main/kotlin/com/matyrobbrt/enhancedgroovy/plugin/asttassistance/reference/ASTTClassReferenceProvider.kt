package com.matyrobbrt.enhancedgroovy.plugin.asttassistance.reference

import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.stream
import java.util.stream.Collectors

class ASTTClassReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element is PsiLiteral && element.value is String) {
            return arrayOf(Reference(element, element.value as String))
        } else if (element is PsiArrayInitializerMemberValue) {
            return element.initializers.stream()
                .filter { it is PsiLiteral && it.value is String }
                .map { it as PsiLiteral }
                .map { Reference(it, it.value as String) }
                .collect(Collectors.toList())
                .toTypedArray()
        }
        return PsiReference.EMPTY_ARRAY
    }

    private class Reference(element: PsiLiteral, private val name: String) : PsiReferenceBase<PsiLiteral>(element) {
        override fun resolve() =
            JavaPsiFacade.getInstance(element.project).findClass(name, element.resolveScope)
    }
}