package io.github.jetjava.rustengine.internal;

import java.nio.file.Path;
import java.util.Objects;

import io.github.jetjava.rustengine.RustEngine;

public final class NativeRustEngine implements RustEngine {

    private final Path nativeCacheDirectory;

    public NativeRustEngine(Path nativeCacheDirectory) {
        this.nativeCacheDirectory = Objects.requireNonNull(
                nativeCacheDirectory,
                "nativeCacheDirectory"
        );
    }

    @Override
    public long add(long a, long b) {
        return RustBindings.add(nativeCacheDirectory, a, b);
    }
}
