package com.eventpro.SalesService.service;

import java.util.List;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.SalesService.enums.SaleStatusEnum;

public interface SaleService {
	
	public void create(final SaleDTO sale);
	
	public List<SaleDTO> findAll(final Integer eventId, final Integer attendeeId, final String status);
	
	public SaleDTO updateStatus(final Integer eventId, final Integer attendeeId, final SaleStatusEnum status);
}
