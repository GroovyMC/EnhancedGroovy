package com.matyrobbrt.enhancedgroovy.plugin.util

import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.groovydoc.psi.api.GrDocComment
import org.jetbrains.plugins.groovy.lang.psi.impl.synthetic.GrLightField

class GroovyLightField(
    name: String,
    type: String,
    context: PsiElement,

    var docs: GrDocComment? = null
) : GrLightField(name, type, context) {
    override fun getDocComment() = docs
}