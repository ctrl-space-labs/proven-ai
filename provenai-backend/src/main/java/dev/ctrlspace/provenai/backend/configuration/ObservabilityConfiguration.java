package dev.ctrlspace.provenai.backend.configuration;

import dev.ctrlspace.provenai.backend.observations.LoggingObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ObservabilityConfiguration {

    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry, LoggingObservationHandler loggingObservationHandler) {

        return new ObservedAspect(observationRegistry);
    }




}