package lachy.training.beerservice.web.mappers;

import lachy.training.beerservice.web.models.BeerDto;
import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.services.inventory.BeerInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BeerMapperDecorator implements BeerMapper {

    private BeerInventoryService beerInventoryService;
    private BeerMapper mapper;

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @Autowired
    public void setMapper(BeerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BeerDto toDto(Beer beer) {
        return mapper.toDto(beer);
    }

    @Override
    public BeerDto toDtoWithInventory(Beer beer) {
        BeerDto dto = mapper.toDto(beer);
        dto.setQuantityOnHand(beerInventoryService.getOnhandInventory(beer.getId()));
        return dto;
    }

    @Override
    public Beer toDomain(BeerDto beerDto) {
        return mapper.toDomain(beerDto);
    }
}
