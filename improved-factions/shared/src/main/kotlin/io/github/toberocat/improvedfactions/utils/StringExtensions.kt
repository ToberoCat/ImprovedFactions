package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.annotations.command.CommandResponse


fun String.camlCaseToSnakeCase(separator: String = "_") =
    replace(Regex("([a-z])([A-Z]+)"), "$1${separator}$2").lowercase()

fun String.labelToSnakeCase(separator: String = "_") = split(" ").joinToString(separator).lowercase()

fun String.convertToCamelCase() = lowercase()
    .replace(Regex("-(\\w)")) { matchResult ->
        matchResult.groupValues[1].uppercase()
    }


fun List<*>.toKotlinString() = joinToString(", ", "listOf(", ")")

fun CommandResponse.toKotlinString() = """CommandResponse(responseName="$responseName", key="$key")"""