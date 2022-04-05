package com.api.monnos.commons;

import com.api.monnos.model.Planet;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.util.List;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class PlanetResponseBody {

    private String description;

    private List<Planet> planets;

    private Planet planet;

}
