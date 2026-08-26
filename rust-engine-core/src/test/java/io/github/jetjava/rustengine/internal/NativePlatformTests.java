package io.github.jetjava.rustengine.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NativePlatformTests {

    @ParameterizedTest
    @CsvSource({
            "Windows 11, amd64, windows-x86_64, engine.dll",
            "Windows 11, aarch64, windows-aarch64, engine.dll",
            "Linux, x86_64, linux-x86_64, libengine.so",
            "Linux, arm64, linux-aarch64, libengine.so",
            "Mac OS X, x86_64, macos-x86_64, libengine.dylib",
            "Darwin, aarch64, macos-aarch64, libengine.dylib"
    })
    void resolvesNativeLibrary(
            String osName,
            String architecture,
            String resourceDirectory,
            String libraryFileName
    ) {
        NativePlatform platform = NativePlatform.from(osName, architecture);

        assertEquals(libraryFileName, platform.libraryFileName());
        assertEquals(
                "/META-INF/native/" + resourceDirectory + "/" + libraryFileName,
                platform.resourcePath()
        );
    }

    @Test
    void rejectsUnsupportedOperatingSystem() {
        assertThrows(
                IllegalStateException.class,
                () -> NativePlatform.from("FreeBSD", "amd64")
        );
    }

    @Test
    void rejectsUnsupportedArchitecture() {
        assertThrows(
                IllegalStateException.class,
                () -> NativePlatform.from("Linux", "riscv64")
        );
    }
}
