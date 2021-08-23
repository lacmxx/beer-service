package lachy.training.beerservice.services;

import lachy.training.beerservice.web.models.BeerDto;
import lachy.training.beerservice.web.models.BeerPagedList;
import lachy.training.beerservice.web.models.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    BeerPagedList listBeers(
            String beerName,
            BeerStyleEnum beerStyle,
            PageRequest of,
            Boolean showInventoryOnHand);

    List<BeerDto> listAll();

    BeerDto getBeerById(UUID beerId, boolean showInventoryOnHand);

    BeerDto getBeerByUpc(String upc, boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID id, BeerDto beerDto);

    void deleteBeerById(UUID beerId);


}
