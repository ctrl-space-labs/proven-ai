package dev.ctrlspace.provenai.backend.configuration;

import dev.ctrlspace.provenai.backend.adapters.GendoxProfileAdapter;
import dev.ctrlspace.provenai.backend.authentication.KeycloakAuthenticationService;
import dev.ctrlspace.provenai.backend.controller.DataPodController;
import dev.ctrlspace.provenai.backend.converters.OrganizationConverter;
import dev.ctrlspace.provenai.backend.exceptions.ResponseControllerAdvice;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.observations.LoggingObservationHandler;
import dev.ctrlspace.provenai.backend.repositories.DataPodRepository;
import dev.ctrlspace.provenai.backend.services.DataPodService;
import dev.ctrlspace.provenai.backend.utils.JWTUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
		GendoxProfileAdapter.class,
		DataPodController.class,
		DataPodRepository.class,
		DataPodService.class,
		JWTUtils.class,
		LoggingObservationHandler.class,
		ProvenAiBackendApplication.class,
		ResponseControllerAdvice.class,
		OrganizationConverter.class,
		KeycloakAuthenticationService.class

})
@EnableCaching
@EnableJpaRepositories(basePackageClasses = {DataPodRepository.class})
@EntityScan(basePackageClasses = {DataPod.class})
public class ProvenAiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvenAiBackendApplication.class, args);
	}

}
