package com.matyrobbrt.enhancedgroovy.plugin.util

import com.intellij.psi.PsiClass

fun implements(clazz: PsiClass, toImplement: String): Boolean {
    if (clazz.qualifiedName == toImplement) return true
    if (clazz.implementsList?.referencedTypes?.any {implements(it.resolve() ?: return@any false, toImplement)} == true) return true
    if (clazz.extendsList?.referencedTypes?.any {implements(it.resolve() ?: return@any false, toImplement)} == true) return true
    return false
}