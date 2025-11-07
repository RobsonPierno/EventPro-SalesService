package com.eventpro.SalesService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventpro.SalesService.model.Sale;
import com.eventpro.SalesService.model.SaleId;

import jakarta.persistence.LockModeType;

@Repository
public interface SaleRepository extends JpaRepository<Sale, SaleId>, JpaSpecificationExecutor<Sale> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.ticketId = :ticketId")
	Integer countByTicketId(@Param("ticketId")Integer ticketId);

}
