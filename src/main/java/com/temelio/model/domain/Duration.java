package com.temelio.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Duration {
	@JsonProperty("grandStart")
	private String grandStart;

	@JsonProperty("grandEnd")
	private String grandEnd;

}
