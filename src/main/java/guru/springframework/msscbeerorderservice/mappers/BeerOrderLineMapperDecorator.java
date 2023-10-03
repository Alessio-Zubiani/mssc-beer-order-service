package guru.springframework.msscbeerorderservice.mappers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import guru.springframework.msscbeerorderservice.domain.BeerOrderLine;
import guru.springframework.msscbeerorderservice.services.beer.BeerService;
import guru.springframework.msscbeerorderservice.web.model.BeerDto;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderLineDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {
	
	private BeerService beerService;
	private BeerOrderLineMapper beerOrderLineMapper;
	
	@Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineMapper = beerOrderLineMapper;
    }
	

	@Override
	public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
		
		BeerOrderLineDto orderLineDto = this.beerOrderLineMapper.beerOrderLineToDto(line);
		Optional<BeerDto> o = this.beerService.getBeerByUpc(line.getUpc());
		
		o.ifPresent(beerDto -> {
			log.debug("Dto: [{}]", beerDto);
			orderLineDto.setBeerId(beerDto.getId());
			orderLineDto.setBeerName(beerDto.getBeerName());
			orderLineDto.setBeerStyle(beerDto.getBeerStyle().name());
			orderLineDto.setPrice(beerDto.getPrice());
		});
		
		return orderLineDto;
	}

}
