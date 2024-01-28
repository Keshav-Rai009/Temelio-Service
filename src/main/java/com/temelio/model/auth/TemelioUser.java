package com.temelio.model.auth;

import java.security.Principal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemelioUser implements Principal {

	private UUID userId;
	private String username;
	private String password;

	@Override
	public String getName() {
		return this.username;
	}
}
