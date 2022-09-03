package com.matyrobbrt.enhancedgroovy.plugin.asttassistance.reference

import com.intellij.patterns.PsiJavaPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class ASTTReferenceContributor : PsiReferenceContributor() {
    companion object {
        const val asttAnnotationName = "org.codehaus.groovy.transform.GroovyASTTransformationClass"
    }
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(StandardPatterns.or(PsiJavaPatterns.psiLiteral().annotationParam(
            asttAnnotationName, "value"
        ), PsiJavaPatterns.psiElement(PsiArrayInitializerMemberValue::class.java).annotationParam(
            asttAnnotationName, "value"
        )), ASTTClassReferenceProvider())
    }
}