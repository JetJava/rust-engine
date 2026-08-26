package io.github.jetjava.rustengine.autoconfigure;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import io.github.jetjava.rustengine.RustEngine;

import static org.assertj.core.api.Assertions.assertThat;

class RustEngineAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(RustEngineAutoConfiguration.class)
                    );

    @Test
    void createsRustEngineByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RustEngine.class);
            assertThat(context).hasSingleBean(RustEngineProperties.class);
            assertThat(context.getBean(RustEngine.class).add(20, 22)).isEqualTo(42);
        });
    }

    @Test
    void backsOffWhenRustEngineBeanExists() {
        RustEngine customEngine = Math::addExact;

        contextRunner
                .withBean(RustEngine.class, () -> customEngine)
                .run(context -> assertThat(context.getBean(RustEngine.class))
                        .isSameAs(customEngine));
    }

    @Test
    void canBeDisabled() {
        contextRunner
                .withPropertyValues("rust-engine.enabled=false")
                .run(context -> assertThat(context)
                        .doesNotHaveBean(RustEngine.class));
    }

    @Test
    void bindsNativeCacheDirectory() {
        contextRunner
                .withPropertyValues(
                        "rust-engine.native-cache-directory=build/native-cache"
                )
                .run(context -> assertThat(
                        context.getBean(RustEngineProperties.class)
                                .getNativeCacheDirectory()
                ).isEqualTo(Path.of("build/native-cache")));
    }
}
