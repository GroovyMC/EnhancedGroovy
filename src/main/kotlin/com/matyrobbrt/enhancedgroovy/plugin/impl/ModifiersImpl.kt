/*
 * EnhancedGroovy
 * Copyright (C) 2022 Matyrobbrt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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