package io.github.toberocat.improvedfactions.extentions;

import org.xeustechnologies.jcl.JarClassLoader;

public class ExtensionContainer {

    private Extension extension;
    private JarClassLoader jcl;

    public ExtensionContainer(Extension extension, JarClassLoader jcl) {
        this.extension = extension;
        this.jcl = jcl;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public JarClassLoader getJcl() {
        return jcl;
    }

    public void setJcl(JarClassLoader jcl) {
        this.jcl = jcl;
    }
}
