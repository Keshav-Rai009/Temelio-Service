package com.temelio.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temelio.model.domain.Duration;
import com.temelio.model.domain.GrandSubmission;
import com.temelio.model.domain.NonProfit;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GrandSubmissionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrandSubmissionsService.class);
	private final NonProfitsService nonProfitsService;

	private boolean isValidStartAndEndDuration(final Duration grandDuration) {
		final String grandStartDate = this.formatGrandDuration(grandDuration.getGrandStart().replaceAll("/", "-"));
		final String grandEndDate = this.formatGrandDuration(grandDuration.getGrandEnd().replaceAll("/", "-"));

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		final LocalDate startDate = LocalDate.parse(grandStartDate, formatter);
		final LocalDate endDate = LocalDate.parse(grandEndDate, formatter);
		return endDate.isAfter(startDate);
	}

	private String formatGrandDuration(final String date) {
		final String[] dateParts = date.split("-");
		String month = dateParts[0];
		String day = dateParts[1];

		if (month.length() == 1) {
			month = "0".concat(month);
		}

		if (day.length() == 1) {
			day = "0".concat(day);
		}

		// MM-dd-yyyy format
		return month + '-' + day + '-' + dateParts[2];
	}


	/**
	 * @return a list of submissions for a nonprofit
	 */
	public final Response getGrandSubmissions(final UUID nonProfitId) {
		final NonProfit nonProfit = this.nonProfitsService.getNonProfitsMap().get(nonProfitId);

		if (nonProfit == null) {
			LOGGER.error("Invalid non profit id:" + nonProfitId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		final List<GrandSubmission> nonProfitGrandSubmissions = nonProfit.getGrandSubmissions();
		return Response.ok(nonProfitGrandSubmissions).build();
	}

	/**
	 * Creates a new submission for a nonprofit
	 */
	public final Response addGrandSubmission(final UUID nonProfitId, final GrandSubmission grandSubmission) {
		final NonProfit nonProfit = this.nonProfitsService.getNonProfitsMap().get(nonProfitId);

		if (nonProfit == null) {
			LOGGER.error("Invalid non profit id:" + nonProfitId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		if (grandSubmission.getNonProfitId() != null && nonProfitId != grandSubmission.getNonProfitId()) {
			LOGGER.error("Grand submission's non profit id and path parameter non profit id mismatch");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// The grand submission should have same parent non-profit name
		if (!grandSubmission.getNonProfitName().equals(nonProfit.getLegalName())) {
			LOGGER.error("Grand submission should have same parent non-profit name.");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// a valid grand submission should have an end date after start date
		if (!this.isValidStartAndEndDuration(grandSubmission.getDuration())) {
			LOGGER.error("Invalid start and end duration");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		final UUID grandSubmissionId = UUID.randomUUID();
		final Optional<GrandSubmission> existingGrandSubmission =
				nonProfit.getGrandSubmissions().stream().filter(submission ->
						submission.getId().equals(grandSubmissionId)).findFirst();
		// in case of collision - should never happen though - UUID has 0.00000006 collision probability
		if (!existingGrandSubmission.isEmpty()) {
			return this.updateGrandSubmission(nonProfitId, grandSubmissionId, existingGrandSubmission.get());
		}

		// submission id is autogenerated everytime
		grandSubmission.setId(grandSubmissionId);
		// in case non-profit id is not provided in request body
		grandSubmission.setNonProfitId(nonProfitId);
		nonProfit.getGrandSubmissions().add(grandSubmission);
		return Response.ok(grandSubmission).build();
	}

	/**
	 * @retuns a submission by id for a nonprofit
	 * @param: Non Profit id
	 * @param: submission id
	 */
	public final Response getGrandSubmission(final UUID nonProfitId, final UUID grandSubmissionId) {
		final NonProfit nonProfit = this.nonProfitsService.getNonProfitsMap().get(nonProfitId);

		if (nonProfit == null) {
			LOGGER.error("Invalid non profit id:" + nonProfitId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		final Optional<GrandSubmission> grandSubmission =
				nonProfit.getGrandSubmissions().stream().filter(submission ->
						submission.getId().equals(grandSubmissionId)).findFirst();

		if (grandSubmission.isEmpty()) {
			LOGGER.error("Invalid grand submission id:" + grandSubmissionId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.ok(grandSubmission).build();
	}

	/**
	 * Updates a submission by id for a nonprofit
	 *
	 * @param: Non Profit id
	 * @param: submission id
	 */
	public final Response updateGrandSubmission(final UUID nonProfitId, final UUID grandSubmissionId,
	                                            final GrandSubmission grandSubmission) {
		final NonProfit nonProfit = this.nonProfitsService.getNonProfitsMap().get(nonProfitId);

		if (nonProfit == null) {
			LOGGER.error("Invalid non profit id:" + nonProfitId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// The grand submission should have same parent non-profit name
		if (!grandSubmission.getNonProfitName().equals(nonProfit.getLegalName())) {
			LOGGER.error("Grand submission should have same parent non-profit name.");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		final Optional<GrandSubmission> grandSubmissionToBeUpdated =
				nonProfit.getGrandSubmissions().stream().filter(submission ->
						submission.getId().equals(grandSubmissionId)).findFirst();

		if (grandSubmissionToBeUpdated.isEmpty()) {
			LOGGER.error("Invalid grand submission id:" + grandSubmissionId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// a valid grand submission should have an end date after start date
		if (!this.isValidStartAndEndDuration(grandSubmission.getDuration())) {
			LOGGER.error("Invalid start and end duration");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		final GrandSubmission updatedGrandSubmission = grandSubmissionToBeUpdated.get();
		updatedGrandSubmission.setGrandName(grandSubmission.getGrandName());
		updatedGrandSubmission.setGrandType(grandSubmission.getGrandType());
		updatedGrandSubmission.setRequestedAmount(grandSubmission.getRequestedAmount());
		updatedGrandSubmission.setAwardedAmount(grandSubmission.getAwardedAmount());
		updatedGrandSubmission.setTags(grandSubmission.getTags());
		updatedGrandSubmission.setDuration(grandSubmission.getDuration());

		return Response.ok(updatedGrandSubmission).build();
	}

	/**
	 * @return a list of submissions for all nonprofits
	 */
	public final Response getAllGrandSubmissions() {
		final List<GrandSubmission> allGrandSubmissions = new ArrayList<>();
		this.nonProfitsService.getNonProfitsMap().forEach((nonProfitId, nonProfit) ->
				allGrandSubmissions.addAll(nonProfit.getGrandSubmissions()));

		return Response.ok(allGrandSubmissions).build();
	}

}
