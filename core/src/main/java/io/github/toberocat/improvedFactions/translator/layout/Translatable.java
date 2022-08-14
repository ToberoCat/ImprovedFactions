package io.github.toberocat.improvedFactions.translator.layout;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.github.toberocat.improvedFactions.translator.layout.messages.Messages;
import io.github.toberocat.improvedFactions.translator.layout.meta.Meta;
import io.github.toberocat.improvedFactions.translator.layout.ranks.XmlRank;
import io.github.toberocat.improvedFactions.translator.layout.ranks.XmlSingleRank;

import java.util.Map;

@JacksonXmlRootElement(localName = "translatable")
public class Translatable {
    @JacksonXmlProperty(isAttribute = true)
    private String version;

    private Meta meta;
    private Messages messages;
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
}
