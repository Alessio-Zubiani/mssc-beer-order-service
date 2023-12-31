package guru.springframework.msscbeerorderservice.bootstrap;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.msscbeerorderservice.domain.Customer;
import guru.springframework.msscbeerorderservice.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class BeerOrderLoader implements CommandLineRunner {
	
	public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final CustomerRepository customerRepository;

    
    @Override
    public void run(String... args) throws Exception {
        this.loadCustomerData();
    }

    private void loadCustomerData() {
        if (this.customerRepository.count() == 0) {
            this.customerRepository.save(Customer.builder()
                    .customerName(TASTING_ROOM)
                    .apiKey(UUID.randomUUID())
                    .build());
        }
    }

}
