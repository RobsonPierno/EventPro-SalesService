package com.eventpr.SalesService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eventpr.SalesService.dto.SaleDTO;

@FeignClient(name = "sales-service", url = "http://sales:8083", path = "/sales")
public interface SaleClient {

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody final SaleDTO sale);
	
	@GetMapping
	public List<SaleDTO> findAll(@RequestParam(required = false) final Integer ticketId, 
								@RequestParam(required = false) final Integer attendeeId, 
								@RequestParam(required = false) final String status);
	
	@GetMapping("/detail")
	public SaleDTO getDetails(@RequestParam(required = true) final Integer ticketId, @RequestParam(required = true) final Integer attendeeId);
	
}
