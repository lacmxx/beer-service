package lachy.training.beerservice.services;

import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.repositories.BeerRepository;
import lachy.training.beerservice.web.controllers.NotFoundException;
import lachy.training.beerservice.web.mappers.BeerMapper;
import lachy.training.beerservice.web.models.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDto> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BeerDto getBeerById(UUID beerId) {
        log.debug("Find beer with Id: " + beerId);
        return beerRepository.findById(beerId)
                .map(beerMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        Beer beer = beerMapper.toDomain(beerDto);
        return beerMapper.toDto(
                beerRepository.save( beer )
        );
    }

    @Override
    public BeerDto updateBeer(UUID id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .map(beer -> {
                    beer.setName( beerDto.getName() );
                    beer.setStyle( beerDto.getStyle().name() );
                    beer.setPrice( beerDto.getPrice() );
                    beer.setUpc(beerDto.getUpc() );
                    return beerMapper.toDto( beerRepository.save(beer) );
                })
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteBeerById(UUID beerId) {

    }
}
