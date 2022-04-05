package com.api.monnos.commons;

import com.api.monnos.model.SwapiPlanet;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class SwapiPlanetResponseBody {

    public Integer count;
    public Object next;
    public Object previous;

    public List<SwapiPlanet> results;

}
