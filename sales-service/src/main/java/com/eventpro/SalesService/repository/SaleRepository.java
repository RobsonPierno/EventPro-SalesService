package com.eventpro.SalesService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.eventpro.SalesService.model.Sale;
import com.eventpro.SalesService.model.SaleId;

@Repository
public interface SaleRepository extends JpaRepository<Sale, SaleId>, JpaSpecificationExecutor<Sale> {

}
