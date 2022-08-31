package com.matyrobbrt.enhancedgroovy.plugin.impl

import com.intellij.psi.PsiField
import com.matyrobbrt.enhancedgroovy.dsl.members.Field
import com.matyrobbrt.enhancedgroovy.dsl.members.Modifiers

open class ReadOnlyField(
    private val field: PsiField,
    private val mods: Lazy<Modifiers> = lazy { ModifiersImpl(field.modifierList ?: return@lazy ModifiersImpl.simpleImmutable()) }
) : Field {
    override fun getType() = field.type.canonicalText
    override fun getModifiers() = mods.value

    override fun getName() = field.name
    override fun setName(name: String) {
        field.name = name
    }
}