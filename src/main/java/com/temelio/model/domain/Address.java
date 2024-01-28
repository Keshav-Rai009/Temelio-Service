package com.temelio.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
	@JsonProperty("street")
	private String street;

	@JsonProperty("city")
	private String city;

	@JsonProperty("state")
	private String state;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("zip")
	private int zip;

}
