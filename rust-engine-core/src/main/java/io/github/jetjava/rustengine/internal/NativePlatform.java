package io.github.jetjava.rustengine.internal;

import java.util.Locale;

record NativePlatform(
        String operatingSystem,
        String architecture,
        String libraryFileName
) {

    static NativePlatform current() {
        return from(
                System.getProperty("os.name"),
                System.getProperty("os.arch")
        );
    }

    static NativePlatform from(String osName, String architecture) {
        String operatingSystem = detectOperatingSystem(osName);

        return new NativePlatform(
                operatingSystem,
                detectArchitecture(architecture),
                libraryFileName(operatingSystem)
        );
    }

    String resourcePath() {
        return "/META-INF/native/"
                + operatingSystem
                + "-"
                + architecture
                + "/"
                + libraryFileName;
    }

    private static String detectOperatingSystem(String osName) {
        String normalizedOsName = osName.toLowerCase(Locale.ROOT);

        if (normalizedOsName.contains("mac") || normalizedOsName.contains("darwin")) {
            return "macos";
        }
        if (normalizedOsName.contains("win")) {
            return "windows";
        }
        if (normalizedOsName.contains("linux")) {
            return "linux";
        }

        throw new IllegalStateException("Unsupported operating system: " + normalizedOsName);
    }

    private static String detectArchitecture(String architecture) {
        String normalizedArchitecture = architecture.toLowerCase(Locale.ROOT);

        return switch (normalizedArchitecture) {
            case "amd64", "x86_64" -> "x86_64";
            case "aarch64", "arm64" -> "aarch64";
            default -> throw new IllegalStateException(
                    "Unsupported system architecture: " + normalizedArchitecture
            );
        };
    }

    private static String libraryFileName(String operatingSystem) {
        return switch (operatingSystem) {
            case "windows" -> "engine.dll";
            case "linux" -> "libengine.so";
            case "macos" -> "libengine.dylib";
            default -> throw new IllegalStateException(
                    "Unsupported operating system: " + operatingSystem
            );
        };
    }
}
