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

    String getClassName()
}
