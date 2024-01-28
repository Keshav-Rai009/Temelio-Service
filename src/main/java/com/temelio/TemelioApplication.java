package com.temelio;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temelio.auth.TemelioServiceAuthenticator;
import com.temelio.auth.TemelioServiceAuthorizer;
import com.temelio.controller.GrandSubmissionsController;
import com.temelio.controller.NonProfitsController;
import com.temelio.model.auth.TemelioUser;
import com.temelio.service.GrandSubmissionsService;
import com.temelio.service.HealthCheckService;
import com.temelio.service.NonProfitsService;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;


public class TemelioApplication extends Application<TemelioConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemelioApplication.class);
	private static final String BEARER = "Bearer";

	public static void main(final String[] args) throws Exception {
		new TemelioApplication().run("server", "config.yml");
	}

	@Override
	public void run(final TemelioConfiguration temelioConfiguration, final Environment environment) {
		LOGGER.info("Registering temelio authentication service");
		// register OAuth
		environment.jersey().register(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<TemelioUser>().
				setAuthenticator(new TemelioServiceAuthenticator()).
				setAuthorizer(new TemelioServiceAuthorizer()).
				setPrefix(BEARER).buildAuthFilter()));
		environment.jersey().register(RolesAllowedDynamicFeature.class);

		LOGGER.info("Registering temelio endpoints");
		// register temelio APIs
		final NonProfitsService nonProfitsService = new NonProfitsService();
		environment.jersey().register(new NonProfitsController(nonProfitsService));

		final GrandSubmissionsService grandSubmissionsService = new GrandSubmissionsService(nonProfitsService);
		environment.jersey().register(new GrandSubmissionsController(grandSubmissionsService));

		// register app health check
		LOGGER.info("Registering temelio health check endpoint");
		environment.healthChecks().register("Temelio-Service-Health", new HealthCheckService(nonProfitsService));

		// show error details for API exceptions
		environment.jersey().register(new JsonProcessingExceptionMapper(true));
	}

	@Override
	public void initialize(final Bootstrap<TemelioConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
		super.initialize(bootstrap);
	}
}
