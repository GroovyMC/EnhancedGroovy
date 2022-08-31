package com.matyrobbrt.enhancedgroovy.dsl.members

interface Modifiers {
    boolean hasModifier(String name)
    void addModifier(String name)
    void removeModifier(String name)
}