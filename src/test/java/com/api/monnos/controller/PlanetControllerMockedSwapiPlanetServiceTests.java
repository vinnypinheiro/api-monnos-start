package com.api.monnos.controller;

import com.api.monnos.PlanetApplication;
import com.api.monnos.commons.PlanetResponseBody;
import com.api.monnos.commons.Utility;
import com.api.monnos.model.Planet;
import com.api.monnos.model.SwapiPlanet;
import com.api.monnos.repository.PlanetRepository;
import com.api.monnos.service.SwapiPlanetService;
import com.api.monnos.validation.SwapiValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlanetApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PlanetControllerMockedSwapiPlanetServiceTests {

    @Autowired
    PlanetRepository planetRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SwapiPlanetService swapiPlanetServiceMocked;

    @Before
    public void init() {
        planetRepository.deleteAll();
    }

    @Test
    public void cadastrarPlanetaComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        SwapiPlanet swapiPlanet = Utility.getSwapiPlanetDeUmPlaneta(planet);

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenReturn(swapiPlanet);

        String planetaJson = Utility.getJsonCadastroPlaneta(planet);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planets"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
        Assert.assertEquals(planet.getNome(), exchange.getBody().getPlanet().getNome());
        Assert.assertEquals(planet.getClima(), exchange.getBody().getPlanet().getClima());
        Assert.assertEquals(planet.getTerreno(), exchange.getBody().getPlanet().getTerreno());
        Assert.assertEquals(
                java.util.Optional.ofNullable(swapiPlanet.getFilms().size()),
                java.util.Optional.ofNullable(exchange.getBody().getPlanet().getFilmesTotal())
        );
    }

    @Test
    public void cadastrarPlanetaComErroPorDuplicidade() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        this.cadastrarPlanetaComSwapiPlanetServiceMocked(planet);

        RequestEntity<String> entity2 =  RequestEntity
                .post(new URI("/planets"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Utility.getJsonCadastroPlaneta(planet));

        ResponseEntity<PlanetResponseBody> exchange2 = restTemplate
                .exchange(entity2, PlanetResponseBody.class);

        Assert.assertNotNull(exchange2);
        Assert.assertEquals(HttpStatus.CONFLICT, exchange2.getStatusCode());
        Assert.assertEquals("Planeta já existente.", exchange2.getBody().getDescription());

    }

    @Test
    public void cadastrarPlanetaComErroPorNomeNaoLocalizadoEmSWAPI() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenThrow(
                new SwapiValidationException(
                        HttpStatus.NOT_FOUND,
                        "Planeta inexistente na API Star Wars"
                )
        );

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        String planetaJson = Utility.getJsonCadastroPlaneta(planet);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planets"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());

    }

    @Test
    public void cadastrarPlanetaComErroPorErroInternoEmSWAPI() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenThrow(
                new SwapiValidationException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Planeta inexistente na API Star Wars"
                )
        );

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        String planetaJson = Utility.getJsonCadastroPlaneta(planet);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planets"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
    }

    @Test
    public void cadastrar3PlanetasComSucessoListandoTodosNoFinal() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        for (Planet planet : planets) {

            ResponseEntity<PlanetResponseBody> exchange = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planet);
        }

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planets"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals(3, exchange.getBody().getPlanets().size());

        for (Planet planet : exchange.getBody().getPlanets()){

            Assert.assertNotNull(planet.getId());
            Assert.assertNotNull(planet.getNome());
            Assert.assertNotNull(planet.getClima());
            Assert.assertNotNull(planet.getTerreno());
            Assert.assertNotNull(planet.getFilmesTotal());
        }
    }

    @Test
    public void buscarPlanetaPorNomeComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        ResponseEntity<PlanetResponseBody> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planet);

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planets/?nome="+ planet.getNome()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.OK, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);

        Assert.assertEquals(
                exchangeCadastro.getBody().getPlanet().getId(),
                exchangeBusca.getBody().getPlanet().getId()
        );
        Assert.assertEquals(planet.getNome(), exchangeBusca.getBody().getPlanet().getNome());
        Assert.assertEquals(planet.getClima(), exchangeBusca.getBody().getPlanet().getClima());
        Assert.assertEquals(planet.getTerreno(), exchangeBusca.getBody().getPlanet().getTerreno());
        Assert.assertEquals(
                exchangeCadastro.getBody().getPlanet().getFilmesTotal(),
                exchangeBusca.getBody().getPlanet().getFilmesTotal()
        );
    }

    @Test
    public void deletarPlanetaPorIdComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        ResponseEntity<PlanetResponseBody> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planet);

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planets/"+exchangeCadastro.getBody().getPlanet().getId()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
    }

    @Test
    public void cadastrar3PlanetasComSucessoDeletandoTodosNoFinal() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        for (Planet planet : planets) {

            this.cadastrarPlanetaComSwapiPlanetServiceMocked(planet);
        }

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planets"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void buscarPlanetaPorIdComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        ResponseEntity<PlanetResponseBody> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planet);

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planets/"+exchangeCadastro.getBody().getPlanet().getId()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.OK, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);

        Assert.assertEquals(
                exchangeCadastro.getBody().getPlanet().getId(),
                exchangeBusca.getBody().getPlanet().getId()
        );
        Assert.assertEquals(planet.getNome(), exchangeBusca.getBody().getPlanet().getNome());
        Assert.assertEquals(planet.getClima(), exchangeBusca.getBody().getPlanet().getClima());
        Assert.assertEquals(planet.getTerreno(), exchangeBusca.getBody().getPlanet().getTerreno());
        Assert.assertEquals(
                exchangeCadastro.getBody().getPlanet().getFilmesTotal(),
                exchangeBusca.getBody().getPlanet().getFilmesTotal()
        );
    }

    /**
     * Método criado para auxiliar no cadastro e testes de cada planeta sem precisar reescrever código em cada teste
     * @param planet
     * @return ResponseEntity<PlanetResponseBody>
     * @throws SwapiValidationException
     * @throws URISyntaxException
     */
    private ResponseEntity<PlanetResponseBody> cadastrarPlanetaComSwapiPlanetServiceMocked(Planet planet) throws
            SwapiValidationException, URISyntaxException {

        SwapiPlanet swapiPlanet = Utility.getSwapiPlanetDeUmPlaneta(planet);

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenReturn(swapiPlanet);

        String planetaJson = Utility.getJsonCadastroPlaneta(planet);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertNotNull(exchange.getBody().getPlanet().getId());
        Assert.assertEquals(planet.getNome(), exchange.getBody().getPlanet().getNome());
        Assert.assertEquals(planet.getClima(), exchange.getBody().getPlanet().getClima());
        Assert.assertEquals(planet.getTerreno(), exchange.getBody().getPlanet().getTerreno());
        Assert.assertEquals(3, exchange.getBody().getPlanet().getFilmesTotal().intValue());

        return exchange;
    }
}
