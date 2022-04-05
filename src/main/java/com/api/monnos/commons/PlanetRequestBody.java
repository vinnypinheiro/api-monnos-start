package com.api.monnos.commons;

import com.api.monnos.model.Planet;
import lombok.*;

import javax.validation.Valid;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class PlanetRequestBody {

    @Valid
    private Planet planet;

}
