package com.matyrobbrt.enhancedgroovy.plugin.impl

import com.intellij.psi.PsiModifierList
import com.matyrobbrt.enhancedgroovy.dsl.members.Modifiers

class ModifiersImpl(private val mods: PsiModifierList): Modifiers {
    companion object {
        fun simpleImmutable(): Modifiers = object : Modifiers {
            override fun hasModifier(name: String?) = false
            override fun addModifier(name: String?) = throw UnsupportedOperationException()
            override fun removeModifier(name: String?) = throw UnsupportedOperationException()
        }
    }
    override fun hasModifier(name: String): Boolean {
        return mods.hasModifierProperty(name)
    }

    override fun addModifier(name: String) {
        mods.setModifierProperty(name, true)
    }

    override fun removeModifier(name: String) {
        mods.setModifierProperty(name, false)
    }
}