package com.eventpro.SalesService;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.eventpr.SalesService.dto.SaleDTO;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = {
		"com.eventpro.EventService.client",
		"com.eventpro.AttendeeService.client"
})
public class SaleServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SaleServiceApplication.class, args);
	}
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String springKafkaBootstrapServers;
	
	@Bean
	public ProducerFactory<String, SaleDTO> ticketSaleCreatedPF() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.springKafkaBootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, SaleDTO> ticketSaleCreatedKT() {
		return new KafkaTemplate<String, SaleDTO>(this.ticketSaleCreatedPF());
	}
}
