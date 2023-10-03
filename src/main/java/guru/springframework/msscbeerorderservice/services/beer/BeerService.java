package guru.springframework.msscbeerorderservice.services.beer;

import java.util.Optional;
import java.util.UUID;

import guru.springframework.msscbeerorderservice.web.model.BeerDto;

public interface BeerService {
	
	Optional<BeerDto> getBeerById(UUID beerId);
	
	Optional<BeerDto> getBeerByUpc(String upc);

}
