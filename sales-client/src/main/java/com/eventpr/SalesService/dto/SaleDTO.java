package com.eventpr.SalesService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleDTO(Integer ticketId, String eventName, String ticketType, Integer attendeeId, String attendeeName, BigDecimal price, LocalDateTime purchaseDate, String status) {

}
