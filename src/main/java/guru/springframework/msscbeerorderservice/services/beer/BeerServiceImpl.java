package guru.springframework.msscbeerorderservice.services.beer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import guru.springframework.msscbeerorderservice.web.model.BeerDto;
import lombok.Setter;

@Setter

@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Service
public class BeerServiceImpl implements BeerService {
	
	private final String BEER_PATH = "/api/v1/beer/{beerId}";
	private final String BEER_UPC_PATH = "/api/v1/beer/upc/{upc}";
	private final RestTemplate restTemplate;
	
	public BeerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	private String beerServiceHost;

	@Override
	public Optional<BeerDto> getBeerById(UUID beerId) {
		
		Map<String, UUID> uriParam = new HashMap<>();
	    uriParam.put("beerId", beerId);
		
		BeerDto beerDto = this.restTemplate
				.getForObject(this.beerServiceHost.concat(BEER_PATH), BeerDto.class, uriParam);
		
		return Optional.of(beerDto);
	}

	@Override
	public Optional<BeerDto> getBeerByUpc(String upc) {
		
		Map<String, String> uriParam = new HashMap<>();
	    uriParam.put("upc", upc);
        
		BeerDto beerDto = this.restTemplate
				.getForObject(this.beerServiceHost.concat(BEER_UPC_PATH), BeerDto.class, uriParam);
		
		return Optional.of(beerDto);
	}

}
