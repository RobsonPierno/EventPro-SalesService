package com.eventpro.SalesService.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.SalesService.enums.SaleStatusEnum;
import com.eventpro.SalesService.service.SaleService;

@Component
public class KafkaConsumer {
	
	@Autowired
	private SaleService saleService;

	@KafkaListener(
			topics = "${kafka.topic.payment.success}",
			containerFactory = "paymentResultKafkaListener"
	)
	public void paymentSuccess(final SaleDTO saleDto) {
		this.saleService.updateStatus(saleDto.ticketId(), saleDto.attendeeId(), SaleStatusEnum.PAID);
	}
	
	@KafkaListener(
		topics = "${kafka.topic.payment.fail}",
		containerFactory = "paymentResultKafkaListener"
	)
	public void paymentFail(final SaleDTO saleDto) {
		this.saleService.updateStatus(saleDto.ticketId(), saleDto.attendeeId(), SaleStatusEnum.PAYMENT_FAILED);
	}
	
	@KafkaListener(
		topics = "${kafka.topic.ticket.sale.done}",
		containerFactory = "paymentResultKafkaListener"
	)
	public void paymentTicketDone(final SaleDTO saleDto) {
		this.saleService.updateStatus(saleDto.ticketId(), saleDto.attendeeId(), SaleStatusEnum.COMPLETED);
	}
}
