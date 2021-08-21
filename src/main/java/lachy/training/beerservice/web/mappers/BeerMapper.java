package lachy.training.beerservice.web.mappers;

import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.web.models.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface BeerMapper {

    Beer toDomain(BeerDto dto);

    BeerDto toDto(Beer beer);

}
