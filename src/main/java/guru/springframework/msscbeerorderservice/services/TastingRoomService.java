package guru.springframework.msscbeerorderservice.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.msscbeerorderservice.bootstrap.BeerOrderLoader;
import guru.springframework.msscbeerorderservice.domain.Customer;
import guru.springframework.msscbeerorderservice.repositories.BeerOrderRepository;
import guru.springframework.msscbeerorderservice.repositories.CustomerRepository;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderDto;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingRoomService {
	
	private final CustomerRepository customerRepository;
    private final BeerOrderService beerOrderService;
    private final BeerOrderRepository beerOrderRepository;
    private final List<String> beerUpcs = Arrays.asList(BeerOrderLoader.BEER_1_UPC, BeerOrderLoader.BEER_2_UPC, BeerOrderLoader.BEER_3_UPC);
    
    
    @Transactional
    @Scheduled(fixedRate = 120000) // run every 120 seconds
    public void placeTastingRoomOrder() {

        List<Customer> customerList = this.customerRepository.findAllByCustomerNameLike(BeerOrderLoader.TASTING_ROOM);

        if (customerList.size() == 1) { //should be just one
            this.doPlaceOrder(customerList.get(0));
        } else {
            log.error("Too many or too few tasting room customers found");
        }
    }

    private void doPlaceOrder(Customer customer) {
        
    	String beerToOrder = this.getRandomBeerUpc();

        BeerOrderLineDto beerOrderLine = BeerOrderLineDto.builder()
                .upc(beerToOrder)
                .orderQuantity(new Random().nextInt(6)) //todo externalize value to property
                .build();

        List<BeerOrderLineDto> beerOrderLineSet = new ArrayList<>();
        beerOrderLineSet.add(beerOrderLine);

        BeerOrderDto beerOrder = BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef(UUID.randomUUID().toString())
                .beerOrderLines(beerOrderLineSet)
                .build();

        this.beerOrderService.placeOrder(customer.getId(), beerOrder);
    }

    private String getRandomBeerUpc() {
        return this.beerUpcs.get(new Random().nextInt(this.beerUpcs.size() - 0));
    }

}
