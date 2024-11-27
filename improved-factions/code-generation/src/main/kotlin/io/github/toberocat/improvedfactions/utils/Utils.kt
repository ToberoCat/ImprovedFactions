package io.github.toberocat.improvedfactions.utils

fun String.camlCaseToSnakeCase(separator: String = "_") = replace(Regex("([a-z])([A-Z]+)"), "$1${separator}$2").lowercase()
