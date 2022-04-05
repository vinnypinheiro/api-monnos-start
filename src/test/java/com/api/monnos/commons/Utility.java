package com.api.monnos.commons;

import com.api.monnos.model.Planet;
import com.api.monnos.model.SwapiPlanet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Service
public class Utility {

    public static List<Planet> getPlanetasDefault() {

        List<Planet> planets = new ArrayList<>();

        Planet planet1 = Planet.builder()
                .nome("planet1")
                .clima("clima1")
                .terreno("terreno1")
                .build();


        Planet planet2 = Planet.builder()
                .nome("planet2")
                .clima("clima2")
                .terreno("terreno2")
                .build();

        Planet planet3 = Planet.builder()
                .nome("planet3")
                .clima("clima3")
                .terreno("terreno3")
                .build();

        planets.add(planet1);
        planets.add(planet2);
        planets.add(planet3);

        return planets;
    }

    public static String getJsonCadastroPlaneta(Planet planet) {

        String jsonPlaneta = "{  \n" +
                "   \"planet\":{  \n" +
                "      \"nome\":\""+ planet.getNome() +"\",\n" +
                "      \"clima\": \""+ planet.getClima() +"\",\n" +
                "      \"terreno\": \""+ planet.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static String getJsonCadastroPlanetaBadRequest(Planet planet) {

        String jsonPlaneta = "{  \n" +
                "   \"planet\":{  \n" +
                "      \"nome\":\""+ planet.getNome() +"\",\n" +
                "      \"climas\": \""+ planet.getClima() +"\",\n" +
                "      \"terreno\": \""+ planet.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static SwapiPlanet getSwapiPlanetDeUmPlaneta(Planet planet) {

        List<Object> filmes = new ArrayList<>();
        filmes.add("filme 1");
        filmes.add("filme 2");
        filmes.add("filme 3");

        SwapiPlanet swapiPlanet = new SwapiPlanet();
        swapiPlanet.setName(planet.getNome());
        swapiPlanet.setClimate(planet.getClima());
        swapiPlanet.setTerrain(planet.getTerreno());
        swapiPlanet.setFilms(filmes);

        return swapiPlanet;
    }
}
