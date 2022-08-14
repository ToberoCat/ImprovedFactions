package io.github.toberocat.improvedFactions.translator.layout.ranks;

public class XmlRank {
    private XmlSingleRank faction;
    private XmlSingleRank ally;

    public XmlRank() {

    }

    public XmlSingleRank getFaction() {
        return faction;
    }

    public void setFaction(XmlSingleRank faction) {
        this.faction = faction;
    }

    public XmlSingleRank getAlly() {
        return ally;
    }

    public void setAlly(XmlSingleRank ally) {
        this.ally = ally;
    }
}
