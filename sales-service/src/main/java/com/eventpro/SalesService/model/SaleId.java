package com.eventpro.SalesService.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class SaleId {
	
	public SaleId() {}
	
	public SaleId(final Integer ticketId, final Integer attendeeId) {
		this.ticketId = ticketId;
		this.attendeeId = attendeeId;
	}

	private Integer ticketId;
	
	private Integer attendeeId;

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public Integer getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(Integer attendeeId) {
		this.attendeeId = attendeeId;
	}
}
