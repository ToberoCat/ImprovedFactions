package io.github.toberocat.improvedFactions.translator.layout;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.github.toberocat.improvedFactions.translator.layout.meta.Meta;

@JacksonXmlRootElement(localName = "translatable")
public class Translatable {
    @JacksonXmlProperty(isAttribute = true)
    private String version;

    private Meta meta;

    public Translatable() {
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
