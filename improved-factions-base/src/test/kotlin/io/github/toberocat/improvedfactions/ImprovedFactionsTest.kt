package io.github.toberocat.improvedfactions

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach


class ImprovedFactionsTest {
    private lateinit var server: ServerMock
    private lateinit var plugin: ImprovedFactionsPlugin

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(ImprovedFactionsPlugin::class.java)
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }
}