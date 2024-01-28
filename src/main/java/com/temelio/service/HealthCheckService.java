package com.temelio.service;

import com.codahale.metrics.health.HealthCheck;

public class HealthCheckService extends HealthCheck {
	private final NonProfitsService nonProfitsService;

	public HealthCheckService(final NonProfitsService nonProfitsService) {
		this.nonProfitsService = nonProfitsService;
	}

	@Override
	protected Result check() throws Exception {
		return this.nonProfitsService.getNonProfitsMap() != null ? Result.healthy("Running") : Result.unhealthy(
				"Crashed");
	}
}
