package io.github.toberocat.improvedfactions.commands.arguments.primitives

import io.github.toberocat.improvedfactions.commands.arguments.ArgumentParser

class StringArgumentParser : ArgumentParser {
    override fun parse(arg: String) = arg
}