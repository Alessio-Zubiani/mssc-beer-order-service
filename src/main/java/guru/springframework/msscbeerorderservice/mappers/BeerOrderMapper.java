package guru.springframework.msscbeerorderservice.mappers;

import org.mapstruct.Mapper;

import guru.springframework.msscbeerorderservice.domain.BeerOrder;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderDto;

@Mapper(uses = { DateMapper.class } )
public interface BeerOrderMapper {
	
	BeerOrderDto beerOrderToDto(BeerOrder beerOrder);

    BeerOrder dtoToBeerOrder(BeerOrderDto dto);

}
