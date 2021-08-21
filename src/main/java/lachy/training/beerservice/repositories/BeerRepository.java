package lachy.training.beerservice.repositories;

import lachy.training.beerservice.domains.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID>, JpaRepository<Beer, UUID> {
}
