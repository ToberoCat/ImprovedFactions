package io.github.toberocat.improvedfactions.modules.claimparticle

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.modules.claimparticle.config.ClaimParticleModuleConfig
import io.github.toberocat.improvedfactions.modules.claimparticle.handles.RenderParticlesTask

class ClaimParticleModule : BaseModule {
    override val moduleName = MODULE_NAME
    override var isEnabled = false

    val config = ClaimParticleModuleConfig()

    override fun onEnable(plugin: ImprovedFactionsPlugin) {
        RenderParticlesTask(config).runTaskTimer(plugin, config.particleSpawnInterval, config.particleSpawnInterval)
    }

    override fun reloadConfig(plugin: ImprovedFactionsPlugin) {
        config.reload(plugin.config)
    }

    companion object {
        const val MODULE_NAME = "claim-particles"
        fun claimParticlesModule() =
            ImprovedFactionsPlugin.instance.moduleManager.getModule<ClaimParticleModule>(MODULE_NAME)

        fun claimParticlesPair() = MODULE_NAME to ClaimParticleModule()
    }
}