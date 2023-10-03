package guru.springframework.msscbeerorderservice.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import guru.springframework.msscbeerorderservice.domain.BeerOrderLine;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderLineDto;

@Mapper(uses = { DateMapper.class } )
@DecoratedWith(BeerOrderLineMapperDecorator.class)
public interface BeerOrderLineMapper {
	
	BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);

}
