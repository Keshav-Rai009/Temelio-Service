package com.temelio.auth;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.temelio.model.auth.TemelioUser;
import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.container.ContainerRequestContext;

public class TemelioServiceAuthorizer implements Authorizer<TemelioUser> {
	
	@Override
	public boolean authorize(TemelioUser temelioUser, String userRole, @Nullable ContainerRequestContext containerRequestContext) {
		return Objects.nonNull(temelioUser) ? true : false;
	}
}
