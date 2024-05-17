package io.github.toberocat.improvedfactions.modules.aistoryteller

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule

class AiStorytellerModule : BaseModule {

    override val moduleName = MODULE_NAME

    companion object {
        const val MODULE_NAME = "ai-storyteller"

        fun aiSupportModule() =
            (ImprovedFactionsPlugin.modules[MODULE_NAME] as? AiStorytellerModule) ?: throw IllegalStateException()

        fun aiSupportPair() = MODULE_NAME to AiStorytellerModule()
    }
}