package com.matyrobbrt.enhancedgroovy.dsl.members

interface Field {
    String getType()
    Modifiers getModifiers()

    String getName()
    void setName(String name)
}