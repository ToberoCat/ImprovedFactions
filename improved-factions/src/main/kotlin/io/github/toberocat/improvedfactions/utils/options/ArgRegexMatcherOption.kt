package io.github.toberocat.improvedfactions.utils.options

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class ArgRegexMatcherOption(index: Int, private val regex: Regex
) : ArgumentOptions(index) {

    override fun validate(arg: String): Boolean = regex.matches(arg)

    override fun argDoesntMatchKey(): String = "base.exceptions.arg-doesnt-match"
}