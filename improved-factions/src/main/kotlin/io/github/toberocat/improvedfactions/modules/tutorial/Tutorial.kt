package io.github.toberocat.improvedfactions.modules.tutorial

import io.github.toberocat.improvedfactions.modules.tutorial.manager.TutorialManager
import io.github.toberocat.improvedfactions.modules.tutorial.manager.TutorialWorldManager
import io.github.toberocat.improvedfactions.modules.tutorial.manager.TutorialWorldManager.deleteTutorialArea
import io.github.toberocat.improvedfactions.modules.tutorial.scene.Scene
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Tutorial(
    private val player: Player,
    private val plugin: Plugin,
    private val scenes: List<Scene>
) {
    private val area = TutorialWorldManager.createPlayerArea()
    private val oldLocation: Location = player.location
    private val tutorialScope = CoroutineScope(Dispatchers.Default)

    init {
        area.teleportPlayer(player,0, 0, 0)
        TutorialManager.lockPlayerMovement(player)
        tutorialScope.launch { executeNextScene(player, 0) }
    }

    private suspend fun executeNextScene(player: Player, index: Int) {
        if (index >= scenes.size) {
            player.sendMessage("Tutorial completed!")
            return
        }

        val scene = scenes[index]
        scene.area = area
        scene.start()
        executeNextScene(player, index + 1)
    }

    private fun completeTutorial() {
        TutorialManager.unlockPlayerMovement(player)
        player.teleport(oldLocation)
        area.deleteTutorialArea()
    }
}
