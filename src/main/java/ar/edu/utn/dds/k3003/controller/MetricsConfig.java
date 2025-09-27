package ar.edu.utn.dds.k3003.controller;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("deploy")
public class MetricsConfig {
    @Bean
    public MeterRegistry datadogMeterRegistry() {
        return new DDMetricsUtils("Solicitudes").getRegistry();
    }
}
