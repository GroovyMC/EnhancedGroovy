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