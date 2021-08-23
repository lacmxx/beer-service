package lachy.training.beerservice.web.controllers;

import lachy.training.beerservice.services.BeerService;
import lachy.training.beerservice.web.models.BeerDto;
import lachy.training.beerservice.web.models.BeerPagedList;
import lachy.training.beerservice.web.models.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = {"application/json"}, path="/beer")
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return ResponseEntity.ok(beerList);
    }

    /* @GetMapping
    // public ResponseEntity<List<BeerDto>> getAll(){
        return ResponseEntity.ok(beerService.listAll());
    }*/

    @GetMapping("/beer/{id}")
    public ResponseEntity<BeerDto> get(@PathVariable UUID id,
                                       @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        return ResponseEntity.ok(beerService.getBeerById(id, showInventoryOnHand));
    }

    @GetMapping("/beerUpc/{upc}")
    public ResponseEntity<BeerDto> get(@PathVariable String upc,
                                       @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        return ResponseEntity.ok(beerService.getBeerByUpc(upc, showInventoryOnHand));
    }

    @PostMapping("/beer")
    public ResponseEntity save(@Validated @RequestBody BeerDto beerDto) {
        BeerDto savedDto = beerService.saveNewBeer(beerDto);
        return ResponseEntity.created(URI.create(String.format("/api/v1/beer/%s", savedDto.getId()))).build();
    }

    @PutMapping("/beer/{id}")
    public ResponseEntity update(@PathVariable UUID id, @Validated @RequestBody BeerDto beerDto) {
        beerService.updateBeer(id, beerDto);
        return ResponseEntity.noContent().build();
    }

}
