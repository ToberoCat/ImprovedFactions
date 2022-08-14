package io.github.toberocat.improvedFactions.translator.layout.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.toberocat.improvedFactions.translator.layout.meta.contributor.AbstractContributor;

import java.util.List;

public class Meta {
    @JsonProperty("supported-languages")
    private List<String> languages;
    private List<AbstractContributor> contributors;

    public Meta() {
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<AbstractContributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<AbstractContributor> contributors) {
        this.contributors = contributors;
    }
}
