package io.github.jetjava.rustengine.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import io.github.jetjava.rustengine.RustEngine;
import io.github.jetjava.rustengine.internal.NativeRustEngine;

@AutoConfiguration
@EnableConfigurationProperties(RustEngineProperties.class)
@ConditionalOnProperty(
        prefix = "rust-engine",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class RustEngineAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    RustEngine rustEngine(
            RustEngineProperties properties
    ) {

        return new NativeRustEngine(properties.getNativeCacheDirectory());
    }
}
