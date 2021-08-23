package lachy.training.beerservice.services;

import lachy.training.beerservice.domains.Beer;
import lachy.training.beerservice.repositories.BeerRepository;
import lachy.training.beerservice.web.controllers.NotFoundException;
import lachy.training.beerservice.web.mappers.BeerMapper;
import lachy.training.beerservice.web.models.BeerDto;
import lachy.training.beerservice.web.models.BeerPagedList;
import lachy.training.beerservice.web.models.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {

        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if (StringUtils.hasLength(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByNameAndStyle(beerName, beerStyle, pageRequest);
        } else if (StringUtils.hasLength(beerName) && StringUtils.isEmpty(beerStyle)) {
            //search beer_service name
            beerPage = beerRepository.findAllByName(beerName, pageRequest);
        } else if (!StringUtils.hasLength(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search beer_service style
            beerPage = beerRepository.findAllByStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventoryOnHand){
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::toDtoWithInventory)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::toDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }

        return beerPagedList;
    }

    @Override
    public List<BeerDto> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    public BeerDto getBeerById(UUID beerId, boolean showInventoryOnHand) {

        if ( showInventoryOnHand ){
            log.debug("Find beer with inventory by Id: " + beerId);
            return beerRepository.findById(beerId)
                    .map(beerMapper::toDtoWithInventory)
                    .orElseThrow(NotFoundException::new);

        } else {
            log.debug("Find beer by Id: " + beerId);
            return beerRepository.findById(beerId)
                    .map(beerMapper::toDto)
                    .orElseThrow(NotFoundException::new);
        }
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
