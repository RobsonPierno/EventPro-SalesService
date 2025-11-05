package com.eventpro.SalesService.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class SaleId {
	
	public SaleId() {}
	
	public SaleId(final Integer eventId, final Integer attendeeId) {
		this.eventId = eventId;
		this.attendeeId = attendeeId;
	}

	private Integer eventId;
	
	private Integer attendeeId;

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Integer getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(Integer attendeeId) {
		this.attendeeId = attendeeId;
	}
}
