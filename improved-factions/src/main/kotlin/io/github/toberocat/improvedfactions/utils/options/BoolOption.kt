package io.github.toberocat.improvedfactions.utils.options

class BoolOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = arg == "true" || arg == "false"

    override fun argDoesntMatchKey(): String = "base.exceptions.expected-boolean"
}