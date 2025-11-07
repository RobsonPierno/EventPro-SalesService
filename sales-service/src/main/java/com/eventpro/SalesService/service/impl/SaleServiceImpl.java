package com.eventpro.SalesService.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.AttendeeService.client.AttendeeClient;
import com.eventpro.AttendeeService.dto.AttendeeDTO;
import com.eventpro.EventService.client.EventClient;
import com.eventpro.EventService.client.TicketClient;
import com.eventpro.EventService.dto.EventDTO;
import com.eventpro.EventService.dto.TicketDTO;
import com.eventpro.SalesService.enums.SaleStatusEnum;
import com.eventpro.SalesService.model.Sale;
import com.eventpro.SalesService.model.SaleId;
import com.eventpro.SalesService.producer.KafkaProducer;
import com.eventpro.SalesService.repository.SaleRepository;
import com.eventpro.SalesService.repository.SaleSpecifications;
import com.eventpro.SalesService.service.SaleService;
import com.eventpro.SalesService.utils.SaleMapper;

@Service
public class SaleServiceImpl implements SaleService {
	
	private static final Logger log = LogManager.getLogger(SaleServiceImpl.class);
	
	private final SaleRepository repository;
	private final TicketClient ticketClient;
	private final EventClient eventClient;
	private final AttendeeClient attendeeClient;
	private final SaleMapper mapper;
	private final KafkaProducer kafkaProducer;

	@Autowired
	public SaleServiceImpl(SaleRepository repository, TicketClient ticketClient, EventClient eventClient, AttendeeClient attendeeClient, SaleMapper mapper, KafkaProducer kafkaProducer) {
		this.repository = repository;
		this.ticketClient = ticketClient;
		this.eventClient = eventClient;
		this.attendeeClient = attendeeClient;
		this.mapper = mapper;
		this.kafkaProducer = kafkaProducer;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void create(final SaleDTO sale) {
		log.debug("create({})", sale);
		LocalTime start = LocalTime.now();
		
		TicketDTO ticket = this.ticketClient.findById(sale.ticketId());
		log.debug("Ticket Exists! | ticketId: {}", sale.ticketId());
		
		this.attendeeClient.findById(sale.attendeeId());
		log.debug("Attendee Exists! | attendeeId: {}", sale.attendeeId());
		
		EventDTO event = this.eventClient.findById(ticket.eventId());
		Integer maxPeople = event.maxPeople();
		log.debug("The maximum number of people at the event is {}!", maxPeople);
		
		Integer qtdAlreadySolde = this.repository.countByTicketId(sale.ticketId());
		log.debug("{} tickets were solde until now!", qtdAlreadySolde);
		
		if (qtdAlreadySolde + 1 >= maxPeople) {
			String message = String.format("The maximum number of people at the event is %s, and {} tickets were solde until now, so no tickets available anymore", maxPeople, qtdAlreadySolde);
			throw new RuntimeException(message);
		}
		
		Sale entity = this.mapper.toEntity(sale);
		entity.setStatus(SaleStatusEnum.PENDING_PAYMENT);
		
		this.repository.save(entity);
		
		this.kafkaProducer.ticketSaleCreated(sale);
		
		LocalTime end = LocalTime.now();
		
		log.debug("Create Sale duration: {}", Duration.between(start, end).getSeconds());
	}

	@Override
	public List<SaleDTO> findAll(final Integer ticketId, final Integer attendeeId, final String status) {
		log.debug("findAll({}, {}, {})", ticketId, attendeeId, status);
		
		var spec = Specification.where(SaleSpecifications.hasTicketId(ticketId))
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

	@Override
	public SaleDTO getDetails(final Integer ticketId, final Integer attendeeId) {
		log.debug("getDetails({}, {})", ticketId, attendeeId);
		
		SaleId saleId = new SaleId(ticketId, attendeeId);
		Sale sale = this.repository.findById(saleId).orElseThrow(RuntimeException::new);

		TicketDTO ticketDto = this.ticketClient.findById(sale.getId().getTicketId());
		String ticketType = ticketDto.type(); 
		BigDecimal price = ticketDto.price();

		EventDTO eventDto = this.eventClient.findById(ticketDto.eventId());
		String eventName = eventDto.name();
		
		AttendeeDTO attDto = this.attendeeClient.findById(sale.getId().getAttendeeId());
		String attendeeName = attDto.name(); 
		
		SaleDTO saleDTO = new SaleDTO(	sale.getId().getTicketId(), 
										eventName, 
										ticketType, 
										sale.getId().getAttendeeId(), 
										attendeeName, 
										price, 
										sale.getPurchaseDate(), 
										sale.getStatus().name());
		
		return saleDTO;
	}

}
