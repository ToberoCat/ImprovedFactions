package io.github.toberocat.improvedfactions.commands.arguments

import kotlin.jvm.Throws

interface ArgumentParser {
    fun parse(arg: String): Any
}