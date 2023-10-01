package guru.springframework.msscbeerorderservice.web.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.msscbeerorderservice.services.BeerOrderService;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderDto;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderPagedList;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/")
@RequiredArgsConstructor
public class BeerOrderController {
	
	private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerOrderService beerOrderService;
    

    @GetMapping("orders")
    public ResponseEntity<BeerOrderPagedList> listOrders(@PathVariable("customerId") UUID customerId,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return new ResponseEntity<BeerOrderPagedList>(
        		this.beerOrderService.listOrders(customerId, PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);
    }

    @PostMapping("orders")
    public ResponseEntity<BeerOrderDto> placeOrder(@PathVariable("customerId") UUID customerId, @RequestBody BeerOrderDto beerOrderDto){
        
    	return new ResponseEntity<BeerOrderDto>(
    			this.beerOrderService.placeOrder(customerId, beerOrderDto), HttpStatus.CREATED);
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<BeerOrderDto> getOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId){
        
    	return new ResponseEntity<BeerOrderDto>(this.beerOrderService.getOrderById(customerId, orderId), HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/pickup")
    public ResponseEntity<Object> pickupOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId){
        
    	this.beerOrderService.pickupOrder(customerId, orderId);
    	return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
