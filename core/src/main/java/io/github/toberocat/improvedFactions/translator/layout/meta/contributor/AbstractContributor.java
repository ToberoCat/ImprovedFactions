package io.github.toberocat.improvedFactions.translator.layout.meta.contributor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeveloperContributor.class, name = "developer"),
        @JsonSubTypes.Type(value = BetaTesterContributor.class, name = "beta-tester")
})
public abstract class AbstractContributor {
    @JsonProperty("contributor")
    @JacksonXmlText
    private String contributorName;

    public AbstractContributor(String contributorName) {
        this.contributorName = contributorName;
    }

    public AbstractContributor() {

    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }
}
