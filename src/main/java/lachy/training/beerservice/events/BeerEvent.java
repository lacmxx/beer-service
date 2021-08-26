package lachy.training.beerservice.events;

import lachy.training.beerservice.web.models.BeerDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -8885037357273365508L;

    private final BeerDto beerDto;

}
