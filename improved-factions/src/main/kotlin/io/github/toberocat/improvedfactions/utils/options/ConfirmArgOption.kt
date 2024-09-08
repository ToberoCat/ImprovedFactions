package io.github.toberocat.improvedfactions.utils.options

class ConfirmArgOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = arg == "confirm"

    override fun argDoesntMatchKey(): String = "base.commands.args.confirmation-needed"
}