package io.github.jetjava.rustengine.autoconfigure;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rust-engine")
public class RustEngineProperties {

    /**
     * Whether to enable the Rust engine.
     */
    private boolean enabled = true;

    /**
     * Directory used to extract and cache the native Rust library.
     */
    private Path nativeCacheDirectory = Path.of(
            System.getProperty("java.io.tmpdir"),
            "rust-engine"
    );

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Path getNativeCacheDirectory() {
        return nativeCacheDirectory;
    }

    public void setNativeCacheDirectory(Path nativeCacheDirectory) {
        this.nativeCacheDirectory = nativeCacheDirectory;
    }
}
