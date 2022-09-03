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

package com.matyrobbrt.enhancedgroovy.plugin.util

import java.nio.file.Path

fun getTopLevelJar(input: String?): Path? {
    if (input != null) {
        val split = input.split("!")
        if (split[0].endsWith(".jar")) return Path.of(split[0])
    }
    return null
}

fun getTopLevelJar(input: Path?): Path? {
    if (input != null) {
        return getAllParents(input).lastOrNull { it.toString().endsWith(".jar") }
    }
    return null
}

fun getAllParents(_input: Path): List<Path> {
    var input = _input
    val parents = ArrayList<Path>()
    while (input.parent != null) {
        parents.add(input.parent)
        input = input.parent
    }
    return parents
}