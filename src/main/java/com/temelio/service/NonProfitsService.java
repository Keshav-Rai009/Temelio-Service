package com.temelio.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temelio.model.domain.NonProfit;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class NonProfitsService {

	private static final ConcurrentHashMap<UUID, NonProfit> nonProfits = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(NonProfitsService.class);


	public ConcurrentHashMap<UUID, NonProfit> getNonProfitsMap() {
		return nonProfits;
	}

	/**
	 * @return the list of all nonprofits
	 */
	public final Response getNonProfits() {
		return Response.ok(this.nonProfits.values()).build();
	}

	/**
	 * Creates a new nonprofit (id is autogenerated)
	 */
	public final Response addNonProfit(final NonProfit nonProfit) {
		final UUID nonProfitId = UUID.randomUUID();
		// in case of collision - should never happen though - UUID has 0.00000006 collision probability
		if (this.getNonProfitsMap().containsKey(nonProfitId)) {
			return this.updateNonProfit(nonProfitId, nonProfit);
		}
		// non-profit id is autogenerated everytime
		nonProfit.setId(nonProfitId);
		this.nonProfits.put(nonProfitId, nonProfit);
		return Response.accepted(nonProfit).build();
	}

	/**
	 * @retuns a nonprofit by id
	 * @param: Non Profit id
	 */
	public final Response getNonProfit(final UUID nonProfitId) {
		final NonProfit nonProfit = this.nonProfits.get(nonProfitId);

		if (nonProfit == null) {
			LOGGER.error("Non profit id" + nonProfitId + "not found");
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.ok(nonProfit).build();
	}

	/**
	 * updates a nonprofit by id
	 *
	 * @param: Non Profit id
	 */
	public final Response updateNonProfit(final UUID nonProfitId, final NonProfit nonProfit) {
		final NonProfit nonProfitToBeUpdated = this.nonProfits.get(nonProfitId);

		if (nonProfitToBeUpdated == null) {
			LOGGER.error("Non Profit to be updated" + nonProfitId + "not found");
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		nonProfit.setId(nonProfitId);
		this.nonProfits.put(nonProfitId, nonProfit);
		return Response.ok(nonProfit).build();
	}

}
