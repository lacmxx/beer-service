package lachy.training.beerservice.events;

import lachy.training.beerservice.web.models.BeerDto;

public class BrewBeerEvent extends BeerEvent {

    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
