package com.matyrobbrt.enhancedgroovy.dsl.members

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

interface Annotation {
    String getType()
    @Nullable
    Object getAttribute(@NotNull String name)
}
