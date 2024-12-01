package io.github.toberocat.improvedfactions.commands.data

import io.github.toberocat.improvedfactions.utils.toKotlinString

data class CommandProcessFunction(
    val senderClass: String,
    val functionName: String,
    val parameters: List<CommandProcessFunctionParameter>
) {
    fun simpleSenderName(): String {
        val name = senderClass.substringAfterLast(".")
        if (name == "CommandSender") return "Console"
        return name
    }

    fun requiredParametersCount() = parameters.count { it.isRequired }

    fun getMinParametersCount(needsConfirmation: Boolean = false): Int {
        return requiredParametersCount() + if (needsConfirmation) 1 else 0
    }

    fun getMaxParametersCount(needsConfirmation: Boolean = false): Int {
        return parameters.size + if (needsConfirmation) 1 else 0
    }

    override fun toString(): String {
        return """CommandProcessFunction(senderClass="$senderClass", functionName="$functionName", parameters=${parameters.toKotlinString()})"""
    }


}