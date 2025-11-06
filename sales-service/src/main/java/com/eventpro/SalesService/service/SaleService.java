package com.eventpro.SalesService.service;

import java.util.List;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.SalesService.enums.SaleStatusEnum;

public interface SaleService {
	
	public void create(final SaleDTO sale);
	
	public List<SaleDTO> findAll(final Integer ticketId, final Integer attendeeId, final String status);
	
	public SaleDTO updateStatus(final Integer ticketId, final Integer attendeeId, final SaleStatusEnum status);
	
	public SaleDTO getDetails(final Integer ticketId, final Integer attendeeId);
}
