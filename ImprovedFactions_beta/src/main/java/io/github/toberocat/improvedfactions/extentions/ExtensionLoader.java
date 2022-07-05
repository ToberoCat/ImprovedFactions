package io.github.toberocat.improvedfactions.extentions;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ExtensionLoader<C> {

    public C LoadClass(File jar, String classpath, Class<C> parentClass) throws ClassNotFoundException {
        try {
            ClassLoader loader = URLClassLoader.newInstance(
                    new URL[] { jar.toURL() },
                    getClass().getClassLoader()
            );
            Class<?> clazz = Class.forName(classpath, true, loader);
            Class<? extends C> newClass = clazz.asSubclass(parentClass);

            Constructor<? extends C> constructor = newClass.getConstructor();
            return constructor.newInstance();

        } catch (MalformedURLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        throw new ClassNotFoundException("Class " + classpath
                + " wasn't found");
    }
}
