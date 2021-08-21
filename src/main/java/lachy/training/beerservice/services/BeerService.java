package lachy.training.beerservice.services;

import lachy.training.beerservice.web.models.BeerDto;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<BeerDto> listAll();

    BeerDto getBeerById(UUID beerId);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID id, BeerDto beerDto);

    void deleteBeerById(UUID beerId);


}
