package com.eventpro.SalesService.repository;

import org.springframework.data.jpa.domain.Specification;

import com.eventpro.SalesService.enums.SaleStatusEnum;
import com.eventpro.SalesService.model.Sale;

public class SaleSpecifications {

	public static final Specification<Sale> hasEventId(final Integer eventId) {
		return (root, query, cb) -> {
			if (eventId == null) return null;
			return cb.equal(root.get("id").get("eventId"), eventId);
		};
	}
	
	public static final Specification<Sale> hasAttendeeId(final Integer attendeeId) {
		return (root, query, cb) -> {
			if (attendeeId == null) return null;
			return cb.equal(root.get("id").get("attendeeId"), attendeeId);
		};
	}
	
	public static final Specification<Sale> hasStatus(final String status) {
		return (root, query, cb) -> {
			if (status == null || status.isBlank()) return null;
			return cb.equal(root.get("status"), SaleStatusEnum.valueOf(status.toUpperCase()));
		};
	}
}
