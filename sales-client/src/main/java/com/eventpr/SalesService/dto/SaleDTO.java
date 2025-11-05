package com.eventpr.SalesService.dto;

import java.time.LocalDateTime;

public record SaleDTO(Integer eventId, Integer attendeeId, LocalDateTime purchaseDate, String status) {

}
