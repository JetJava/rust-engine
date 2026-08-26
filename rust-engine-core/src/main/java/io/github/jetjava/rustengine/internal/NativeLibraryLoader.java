package io.github.jetjava.rustengine.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

final class NativeLibraryLoader {

    private NativeLibraryLoader() {
    }

    static Path extract(Path cacheDirectory) {
        Objects.requireNonNull(cacheDirectory, "cacheDirectory");

        NativePlatform platform = NativePlatform.current();
        Path targetDirectory = cacheDirectory.toAbsolutePath().normalize();
        Path libraryPath = targetDirectory.resolve(platform.libraryFileName());

        try {
            Files.createDirectories(targetDirectory);
            extractResource(platform.resourcePath(), libraryPath);
            return libraryPath;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to extract native Rust library to " + libraryPath,
                    exception
            );
        }
    }

    private static void extractResource(
            String resourcePath,
            Path libraryPath
    ) throws IOException {
        try (InputStream input = NativeLibraryLoader.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Native Rust library was not found at " + resourcePath
                );
            }

            Path temporaryFile = Files.createTempFile(
                    libraryPath.getParent(),
                    libraryPath.getFileName().toString(),
                    ".tmp"
            );

            try {
                Files.copy(input, temporaryFile, StandardCopyOption.REPLACE_EXISTING);
                moveAtomically(temporaryFile, libraryPath);
            } finally {
                Files.deleteIfExists(temporaryFile);
            }
        }
    }

    private static void moveAtomically(
            Path source,
            Path target
    ) throws IOException {
        try {
            Files.move(
                    source,
                    target,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
