package io.github.toberocat.improvedfactions.extentions;

public interface ExtensionDownloadCallback {
    void StartDownload(final ExtensionObject extension);
    void FinishedDownload(final ExtensionObject extension);
}
