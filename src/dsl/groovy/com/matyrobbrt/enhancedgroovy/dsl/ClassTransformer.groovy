package com.matyrobbrt.enhancedgroovy.dsl

import org.jetbrains.annotations.NotNull

interface ClassTransformer {
    void addMethod(@NotNull String name, @NotNull String type)
    void addField(@NotNull String name, @NotNull String type)
}
