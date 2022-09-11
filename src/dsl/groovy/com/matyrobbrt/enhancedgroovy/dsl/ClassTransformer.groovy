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

package com.matyrobbrt.enhancedgroovy.dsl

import com.matyrobbrt.enhancedgroovy.dsl.members.Field
import groovy.transform.NamedParam
import groovy.transform.NamedParams
import org.jetbrains.annotations.NotNull

interface ClassTransformer {
    default void addField(@NotNull String name, @NotNull String type) {
        addField([
                'name': name,
                'type': type
        ])
    }
    void addField(@NamedParams([
        @NamedParam(value = 'name', type = String, required = true),
        @NamedParam(value = 'type', type = String, required = true),
        @NamedParam(value = 'modifiers', type = List<String>),
        @NamedParam(value = 'doc', type = String)
    ]) Map<String, Object> options)
    List<Field> getFields()

    void addMethod(@NamedParams([
            @NamedParam(value = 'name', type = String, required = true),
            @NamedParam(value = 'returnType', type = String),
            @NamedParam(value = 'parameters'),
            @NamedParam(value = 'throws', type = List<String>),
            @NamedParam(value = 'modifiers', type = List<String>)
    ]) Map<String, Object> options)

    String getClassName()
}
