package io.github.toberocat.core.extensions;

public interface ExtensionDownloadCallback {
    void startDownload(final ExtensionObject extension);
    void finishedDownload(final ExtensionObject extension);
}
