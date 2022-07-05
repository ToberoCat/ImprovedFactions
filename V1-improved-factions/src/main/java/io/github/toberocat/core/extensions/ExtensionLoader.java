package io.github.toberocat.core.extensions;

import io.github.toberocat.core.utility.Utility;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ExtensionLoader {

    public static <C> C loadClass(File jar, String classpath, Class<C> parentClass) throws ClassNotFoundException {
        try {
            ClassLoader loader = URLClassLoader.newInstance(
                    new URL[]{jar.toURL()},
                    ExtensionLoader.class.getClassLoader()
            );
            Class<?> clazz = Class.forName(classpath, true, loader);
            Class<? extends C> newClass = clazz.asSubclass(parentClass);

            Constructor<? extends C> constructor = newClass.getConstructor();
            return constructor.newInstance();

        } catch (MalformedURLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            Utility.except(e);
        }

        throw new ClassNotFoundException("Class " + classpath
                + " wasn't found");
    }
}
