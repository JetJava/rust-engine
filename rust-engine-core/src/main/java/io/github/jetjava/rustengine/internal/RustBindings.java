package io.github.jetjava.rustengine.internal;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;

final class RustBindings {

    private static final Object INITIALIZATION_MONITOR = new Object();

    private static volatile MethodHandle addHandle;

    private RustBindings() {
    }

    static long add(
            Path nativeCacheDirectory,
            long a,
            long b
    ) {
        try {
            return (long) getAddHandle(nativeCacheDirectory).invokeExact(a, b);
        } catch (RuntimeException | Error exception) {
            throw exception;
        } catch (Throwable throwable) {
            throw new IllegalStateException("Failed to invoke rust_engine_add", throwable);
        }
    }

    private static MethodHandle getAddHandle(Path nativeCacheDirectory) {
        MethodHandle handle = addHandle;
        if (handle != null) {
            return handle;
        }

        synchronized (INITIALIZATION_MONITOR) {
            if (addHandle == null) {
                addHandle = createAddHandle(nativeCacheDirectory);
            }
            return addHandle;
        }
    }

    private static MethodHandle createAddHandle(Path nativeCacheDirectory) {
        Path libraryPath = NativeLibraryLoader.extract(nativeCacheDirectory);
        SymbolLookup symbols = SymbolLookup.libraryLookup(libraryPath, Arena.global());

        return Linker.nativeLinker().downcallHandle(
                symbols.findOrThrow("rust_engine_add"),
                FunctionDescriptor.of(
                        ValueLayout.JAVA_LONG,
                        ValueLayout.JAVA_LONG,
                        ValueLayout.JAVA_LONG
                )
        );
    }
}
