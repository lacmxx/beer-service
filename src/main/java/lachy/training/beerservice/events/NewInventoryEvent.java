package lachy.training.beerservice.events;

import lachy.training.beerservice.web.models.BeerDto;

public class NewInventoryEvent extends BeerEvent {

    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }

}
