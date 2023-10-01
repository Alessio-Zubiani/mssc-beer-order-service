package guru.springframework.msscbeerorderservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import guru.springframework.msscbeerorderservice.domain.BeerOrderLine;

@Repository
public interface BeerOrderLineRepository extends PagingAndSortingRepository<BeerOrderLine, UUID> {

}
