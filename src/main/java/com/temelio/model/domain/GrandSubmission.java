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
public class GrandSubmission {
	@JsonProperty("id")
	private UUID id;

	@JsonProperty("nonProfitId")
	private UUID nonProfitId;

	@JsonProperty("nonProfitName")
	private String nonProfitName;

	@JsonProperty("grandName")
	private String grandName;

	@JsonProperty("requestedAmount")
	private String requestedAmount;

	@JsonProperty("awardedAmount")
	private String awardedAmount;

	@JsonProperty("grandType")
	private GrandType grandType;

	@JsonProperty("tags")
	private List<String> tags = new ArrayList<>();

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("duration")
	private Duration duration;

	public enum GrandType {
		OPERATING_GRANT,
		PROJECT_GRANT,
		OTHER
	}
}

