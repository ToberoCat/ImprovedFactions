package io.github.toberocat.improvedfactions.unit

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach


open class ImprovedFactionsTest {
    protected lateinit var server: ServerMock
    protected lateinit var plugin: ImprovedFactionsPlugin

    @BeforeEach
    fun setUp() {
        System.setProperty("bstats.relocatecheck", "false")
        server = MockBukkit.mock()
        plugin = MockBukkit.load(ImprovedFactionsPlugin::class.java)
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }
}