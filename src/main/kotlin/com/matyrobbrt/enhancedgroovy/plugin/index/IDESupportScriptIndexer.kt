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

package com.matyrobbrt.enhancedgroovy.plugin.index

import com.intellij.util.indexing.*
import com.intellij.util.io.KeyDescriptor
import com.matyrobbrt.enhancedgroovy.plugin.util.getTopLevelJar
import java.io.DataInput
import java.io.DataOutput
import java.nio.file.Path
import java.util.*

class IDESupportScriptIndexer : ScalarIndexExtension<IDECompatibilityData>() {
    companion object {
        val name = ID.create<IDECompatibilityData, Void>("com.matyrobbrt.enhancedgroovy.supportscripts")
        private val externalizer = object : KeyDescriptor<IDECompatibilityData> {
            override fun getHashCode(value: IDECompatibilityData?): Int {
                return value?.hashCode() ?: 1
            }

            override fun isEqual(val1: IDECompatibilityData?, val2: IDECompatibilityData?): Boolean {
                return val1 == val2
            }

            override fun read(`in`: DataInput): IDECompatibilityData? {
                val annotation = `in`.readUTF()
                if (annotation.isBlank()) return null
                return IDECompatibilityData(annotation, `in`.readBoolean())
            }

            override fun save(out: DataOutput, value: IDECompatibilityData?) {
                out.writeUTF(value?.annotationName ?: "")
                out.writeBoolean(value?.immutable ?: true)
            }
        }

        private val indexer = object : DataIndexer<IDECompatibilityData, Void, FileContent> {
            override fun map(inputData: FileContent): MutableMap<IDECompatibilityData, Void> {
                val canonical: String = inputData.file.canonicalPath ?: return Collections.emptyMap()
                val jarPath: Path? = getTopLevelJar(canonical)?.toAbsolutePath()
                val immutable = jarPath != null
                val path = (if (jarPath == null) "" else "!") + "/enhancedgroovy/"
                val inJarName = canonical.substring(canonical.indexOf(path) + path.length)
                val splitOnDir = inJarName.split("/")
                if (splitOnDir.size == 1) {
                    return Collections.singletonMap(IDECompatibilityData(
                        splitOnDir[0].substring(0, splitOnDir[0].lastIndexOf(".groovy")).replaceFirst("_", ""), // Remove the first _ which only needs to exist in order to prevent generation of classes in the same package
                        immutable
                    ), null)
                }
                return Collections.singletonMap(IDECompatibilityData(splitOnDir[0], immutable), null)
            }
        }
    }

    override fun getName(): ID<IDECompatibilityData, Void> {
        return IDESupportScriptIndexer.name
    }

    override fun getIndexer(): DataIndexer<IDECompatibilityData, Void, FileContent> {
        return IDESupportScriptIndexer.indexer
    }

    override fun getKeyDescriptor(): KeyDescriptor<IDECompatibilityData> {
        return externalizer
    }

    override fun getVersion(): Int {
        return 1
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return FileBasedIndex.InputFilter {
            it.path.contains("enhancedgroovy") && it.extension == "groovy"
        }
    }

    override fun dependsOnFileContent(): Boolean {
        return false
    }
}