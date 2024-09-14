package io.github.toberocat.improvedfactions.modules.tutorial

import io.github.toberocat.improvedfactions.modules.base.BaseModule

object TutorialModule : BaseModule {
    const val MODULE_NAME = "tutorials"
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    fun tutorialModulePair() = moduleName to this
}