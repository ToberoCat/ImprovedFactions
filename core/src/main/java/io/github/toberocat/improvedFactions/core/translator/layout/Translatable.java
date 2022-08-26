package io.github.toberocat.improvedFactions.core.translator.layout;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.github.toberocat.improvedFactions.core.translator.layout.messages.Messages;
import io.github.toberocat.improvedFactions.core.translator.layout.ranks.XmlRank;
import io.github.toberocat.improvedFactions.core.translator.layout.meta.Meta;

import java.util.Map;

@JacksonXmlRootElement(localName = "translatable")
public class Translatable {
    @JacksonXmlProperty(isAttribute = true)
    private String version;

    private Meta meta;
    private Messages messages;
    private String prefix;
    private Map<String, XmlRank> ranks;

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

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public Map<String, XmlRank> getRanks() {
        return ranks;
    }

    public void setRanks(Map<String, XmlRank> ranks) {
        this.ranks = ranks;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
