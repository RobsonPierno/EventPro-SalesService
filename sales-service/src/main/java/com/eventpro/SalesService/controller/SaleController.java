package com.eventpro.SalesService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventpr.SalesService.client.SaleClient;
import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.SalesService.service.SaleService;

@RestController
@RequestMapping("/sales")
public class SaleController implements SaleClient {
	
	@Autowired
	private SaleService service;

	@Override
	public void create(final SaleDTO sale) {
		this.service.create(sale);
	}

	@Override
	public List<SaleDTO> findAll(final Integer eventId, final Integer attendeeId, final String status) {
		List<SaleDTO> dtos = this.service.findAll(eventId, attendeeId, status);
		return dtos;
	}

}
