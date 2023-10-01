package guru.springframework.msscbeerorderservice.services;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.msscbeerorderservice.domain.BeerOrder;
import guru.springframework.msscbeerorderservice.domain.Customer;
import guru.springframework.msscbeerorderservice.mappers.BeerOrderMapper;
import guru.springframework.msscbeerorderservice.repositories.BeerOrderRepository;
import guru.springframework.msscbeerorderservice.repositories.CustomerRepository;
import guru.springframework.msscbeerorderservice.web.exception.NotFoundException;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderDto;
import guru.springframework.msscbeerorderservice.web.model.BeerOrderPagedList;
import guru.springframework.msscbeerorderservice.web.model.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeerOrderServiceImpl implements BeerOrderService {
	
	private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderMapper beerOrderMapper;
	

	@Override
	public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable) {
		
		Optional<Customer> o = this.customerRepository.findById(customerId);
		if(o.isPresent()) {
			Page<BeerOrder> beerOrderPage = this.beerOrderRepository.findAllByCustomer(o.get(), pageable);
			
			return new BeerOrderPagedList(beerOrderPage.getContent()
					.stream()
					.map(this.beerOrderMapper::beerOrderToDto)
					.collect(Collectors.toList()), 
					PageRequest.of(
							beerOrderPage.getPageable().getPageNumber(),
							beerOrderPage.getPageable().getPageSize()
						),
					beerOrderPage.getTotalElements());
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto) {
		
		Optional<Customer> o = this.customerRepository.findById(customerId);
		if(o.isPresent()) {
			BeerOrder beerOrder = this.beerOrderMapper.dtoToBeerOrder(beerOrderDto);
			beerOrder.setId(null);
			beerOrder.setCustomer(o.get());
			beerOrder.setOrderStatus(OrderStatusEnum.NEW);
			
			beerOrder.getBeerOrderLines().forEach(line -> line.setBeerOrder(beerOrder));
			
			BeerOrder savedBeerOrder = this.beerOrderRepository.save(beerOrder);
			log.debug("Saved Beer Order: [{}]", beerOrder.getId());
			
			return this.beerOrderMapper.beerOrderToDto(savedBeerOrder);
		}
		
		throw new NotFoundException(new StringBuilder("Not found Customer with id: [").append(customerId).append("]").toString());
	}

	@Override
	public BeerOrderDto getOrderById(UUID customerId, UUID orderId) {
		
		return this.beerOrderMapper.beerOrderToDto(this.getOrder(customerId, orderId));
	}
	
	@Override
	public void pickupOrder(UUID customerId, UUID orderId) {
		
		BeerOrder beerOrder = this.getOrder(customerId, orderId);
        beerOrder.setOrderStatus(OrderStatusEnum.PICKED_UP);

        this.beerOrderRepository.save(beerOrder);
	}
	
	private BeerOrder getOrder(UUID customerId, UUID orderId) {
		
        Optional<Customer> o = this.customerRepository.findById(customerId);

        if(o.isPresent()){
            Optional<BeerOrder> beerOrderOptional = this.beerOrderRepository.findById(orderId);

            if(beerOrderOptional.isPresent()) {
                BeerOrder beerOrder = beerOrderOptional.get();

                // fall to exception if customer id's do not match - order not for customer
                if(beerOrder.getCustomer().getId().equals(customerId)) {
                    return beerOrder;
                }
            }
            
            throw new NotFoundException(new StringBuilder("Not found Beer Order with id: [").append(orderId).append("]").toString());
        }
        
        throw new NotFoundException(new StringBuilder("Not found Customer with id: [").append(customerId).append("]").toString());
    }

}
