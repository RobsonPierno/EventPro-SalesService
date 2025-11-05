package com.eventpro.SalesService.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.SalesService.model.Sale;

@Mapper(componentModel = "spring", imports = {
		com.eventpro.SalesService.enums.SaleStatusEnum.class,
		com.eventpro.SalesService.model.SaleId.class
})
public interface SaleMapper {

	@Mapping(target = "id", expression = "java(dto.eventId() != null && dto.attendeeId() != null ? new SaleId(dto.eventId(), dto.attendeeId()) : null)")
	@Mapping(target = "status", expression = "java(dto.status() != null ? SaleStatusEnum.valueOf(dto.status().toUpperCase()) : null)")
	public Sale toEntity(SaleDTO dto);
	
	@Mapping(target = "eventId", expression = "java(entity.getId() != null && entity.getId().getEventId() != null ? entity.getId().getEventId() : null)")
	@Mapping(target = "attendeeId", expression = "java(entity.getId() != null && entity.getId().getAttendeeId() != null ? entity.getId().getAttendeeId() : null)")
	@Mapping(target = "status", expression = "java(entity.getStatus() != null ? entity.getStatus().name() : null)")
	public SaleDTO toDTO(Sale entity);
}
