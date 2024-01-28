package com.temelio.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temelio.model.auth.TemelioUser;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemelioServiceAuthenticator implements Authenticator<String, TemelioUser> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemelioServiceAuthenticator.class);


	@SneakyThrows
	@Override
	public Optional<TemelioUser> authenticate(final String token) throws AuthenticationException {
		// can be stored in a key store
		final String masterToken = System.getenv("token");

		if (token.equals(masterToken)) {
			return Optional.of(new TemelioUser());
		}

		LOGGER.error("Wrong credentials provided...");
		return Optional.empty();
	}
}
