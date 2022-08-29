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