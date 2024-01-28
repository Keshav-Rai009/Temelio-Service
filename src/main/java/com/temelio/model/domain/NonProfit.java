package com.temelio.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonProfit {
	@JsonProperty("id")
	private UUID id;

	@JsonProperty("legalName")
	private String legalName;

	@JsonProperty("EIN")
	private String EIN;

	@JsonProperty("mission")
	private String mission;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("address")
	private Address address;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("grandSubmissions")
	private List<GrandSubmission> grandSubmissions = new ArrayList<>();

}
