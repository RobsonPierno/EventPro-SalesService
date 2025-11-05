package com.eventpro.SalesService.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.SalesService.enums.SaleStatusEnum;
import com.eventpro.SalesService.model.Sale;
import com.eventpro.SalesService.model.SaleId;
import com.eventpro.SalesService.repository.SaleRepository;
import com.eventpro.SalesService.repository.SaleSpecifications;
import com.eventpro.SalesService.service.SaleService;
import com.eventpro.SalesService.utils.SaleMapper;

@Service
public class SaleServiceImpl implements SaleService {
	
	private static final Logger log = LogManager.getLogger(SaleServiceImpl.class);
	
	private final SaleRepository repository;
	
	private final SaleMapper mapper;

	@Autowired
	public SaleServiceImpl(SaleRepository repository, SaleMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	@Override
	public void create(final SaleDTO sale) {
		log.debug("create({})", sale);
		
		Sale entity = this.mapper.toEntity(sale);
		entity.setStatus(SaleStatusEnum.PENDING_PAYMENT);
		
		this.repository.save(entity);
	}

	@Override
	public List<SaleDTO> findAll(final Integer eventId, final Integer attendeeId, final String status) {
		log.debug("findAll({}, {}, {})", eventId, attendeeId, status);
		
		var spec = Specification.where(SaleSpecifications.hasEventId(eventId))
					.and(SaleSpecifications.hasAttendeeId(attendeeId))
					.and(SaleSpecifications.hasStatus(status));
		
		List<SaleDTO> sales = this.repository.findAll(spec).stream().map(s -> this.mapper.toDTO(s)).toList();
		
		return sales;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SaleDTO updateStatus(Integer eventId, Integer attendeeId, SaleStatusEnum status) {
		log.debug("findAll({}, {}, {})", eventId, attendeeId, status);
		
		SaleId id = new SaleId(eventId, attendeeId);
		Sale entity = this.repository.findById(id).orElseThrow(RuntimeException::new);
		
		entity.setStatus(status);
		SaleDTO dto = this.mapper.toDTO(entity);
		
		return dto;
	}

}
