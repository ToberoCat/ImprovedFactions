package io.github.toberocat.improvedFactions.core.task;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ChainTest {
    public static void main(String[] args) {
        SharedTask.sharedTask("test", () -> "Hello")
                .async(s -> {
                    System.out.println(s);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "1 s";
                })
                .async((@NotNull Consumer<String>) System.out::println);

        SharedTask.sharedTask("test", () -> "World")
                .async((@NotNull Consumer<String>) System.out::println);
    }

    private void simpleChain() {
        TaskChain.asyncFirst(() -> "Hello")
                .async(s -> {
                    System.out.println(s);
                    return "World";
                })
                .abortIfNull(() -> System.out.println("Aborted"))
                .async(s -> {
                    System.out.println(s);
                    return "Not aborted";
                })
                .async(s -> {
                    System.out.println(s);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "1 s";
                })
                .async((@NotNull Consumer<String>) System.out::println);
        System.out.println("sync");
    }
}
