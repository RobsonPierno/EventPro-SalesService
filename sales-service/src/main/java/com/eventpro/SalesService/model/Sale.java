package com.eventpro.SalesService.model;

import java.time.LocalDateTime;

import com.eventpro.SalesService.enums.SaleStatusEnum;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

@Entity
public class Sale extends Auditing {
	
	@EmbeddedId
	private SaleId id;
	
	private LocalDateTime purchaseDate;
	
	@Enumerated
	private SaleStatusEnum status;

	public SaleId getId() {
		return id;
	}

	public void setId(SaleId id) {
		this.id = id;
	}

	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public SaleStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SaleStatusEnum status) {
		this.status = status;
	}
}
