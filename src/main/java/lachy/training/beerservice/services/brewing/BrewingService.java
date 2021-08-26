package lachy.training.beerservice.services.brewing;

import lachy.training.beerservice.config.JmsConfig;
import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.events.BrewBeerEvent;
import lachy.training.beerservice.repositories.BeerRepository;
import lachy.training.beerservice.services.inventory.BeerInventoryService;
import lachy.training.beerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {

    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper mapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory(){
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            Integer inventoryOnHand = beerInventoryService.getOnhandInventory(beer.getId());

            log.debug("Min on hand is: " + beer.getMinOnHand());
            log.debug("Inventory is: " + inventoryOnHand);
            if( beer.getMinOnHand() >= inventoryOnHand ){
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE, new BrewBeerEvent(mapper.toDto(beer)));
            }

        });
    }

}
