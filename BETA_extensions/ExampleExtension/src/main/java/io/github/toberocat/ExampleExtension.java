package io.github.toberocat;

import io.github.toberocat.core.extensions.Extension;

import java.util.logging.Level;

public class ExampleExtension extends Extension {

    public ExampleExtension() {
    }

    @Override
    protected void onEnable(MainIF plugin) {
        MainIF.logMessage(Level.INFO, "Loaded example extension");
    }

    @Override
    protected void onDisable(MainIF plugin) {

    }
}
