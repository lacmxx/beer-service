package lachy.training.beerservice.repositories;

import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.web.models.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Page<Beer> findAllByName(String beerName, Pageable pageable);

    Page<Beer> findAllByStyle(BeerStyleEnum beerStyle, Pageable pageable);

    Page<Beer> findAllByNameAndStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);

    Optional<Beer> findByUpc(String upc);

}
