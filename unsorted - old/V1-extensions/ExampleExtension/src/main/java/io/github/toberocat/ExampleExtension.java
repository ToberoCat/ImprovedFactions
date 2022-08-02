package io.github.toberocat;

import io.github.toberocat.core.extensions.Extension;

import java.util.logging.Level;

/**
 * Why extensions:
 * Users should be able to install / remove features when they don't need them,
 * but just opening a gui and clicking on the button, waiting, restarting and the command / feature / setting is installed
 *
 * How to install (Dev Mode):
 * Create a folder within the ImprovedFactions folder called Extensions. In there, you need to save the compiled jar file.
 * Don't forget to create a extenion.yml correctly
 *
 * Note: Please disable in the config.yml auto git reports of stack traces, so I won't get spammed with problems,
 * you already checked.
 *  And when you are their, enable printStackTraces, to get the full error log, instead of a message telling you something when wrong
 */
public class ExampleExtension extends Extension {

    public ExampleExtension() {
    }

    @Override
    protected void onEnable(MainIF plugin) { // You can now fully interact with the IF main plugin
        MainIF.logMessage(Level.INFO, "Loaded example extension");
    }

    @Override
    protected void onDisable(MainIF plugin) {
        // A disable methode
    }
}
