package com.temelio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotNull;

public class TemelioConfiguration extends Configuration {
	@NotNull
	private final int defaultSize;

	@JsonCreator
	public TemelioConfiguration(@JsonProperty("defaultSize") final int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public int getDefaultSize() {
		return this.defaultSize;
	}
}
