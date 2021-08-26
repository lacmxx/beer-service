package lachy.training.beerservice.services.brewing;

import lachy.training.beerservice.config.JmsConfig;
import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.events.BrewBeerEvent;
import lachy.training.beerservice.events.NewInventoryEvent;
import lachy.training.beerservice.repositories.BeerRepository;
import lachy.training.beerservice.web.models.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jsmTemplate;

    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event){

        BeerDto beerDto = event.getBeerDto();
        Beer beer = beerRepository.getById(beerDto.getId());
        beerDto.setQuantityOnHand( beer.getQuantityToBrew() );
        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug( "Brewed beer: " + beer.getMinOnHand() + " : QOH: " + beerDto.getQuantityOnHand());

        jsmTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }

}
