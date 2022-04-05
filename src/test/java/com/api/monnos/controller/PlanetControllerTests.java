package com.api.monnos.controller;

import com.api.monnos.repository.PlanetRepository;
import com.api.monnos.validation.SwapiValidationException;
import com.api.monnos.PlanetApplication;
import com.api.monnos.commons.PlanetResponseBody;
import com.api.monnos.commons.Utility;
import com.api.monnos.model.Planet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class PlanetControllerTests {

    @Autowired
    PlanetRepository planetRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void init() {
        planetRepository.deleteAll();
    }

    @Test
    public void cadastrarPlanetaComErroPorBadRequest() throws URISyntaxException {

        List<Planet> planets = Utility.getPlanetasDefault();

        Planet planet = planets.get(0);

        String planetaJson = Utility.getJsonCadastroPlanetaBadRequest(planet);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planets"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }

    @Test
    public void listandoPlanetasSemNenhumCadastradoComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }

    @Test
    public void buscarPlanetaPorNomeComNomeVazioComStatusNotFound() throws URISyntaxException, SwapiValidationException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome="))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
        Assert.assertEquals("Nenhum planeta encontrado", exchangeBusca.getBody().getDescription());
    }

    @Test
    public void buscarPlanetaPorNomeComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome=Tatooine"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }

    @Test
    public void buscarPlanetaPorIDComIdZeroComStatusNotFound() throws URISyntaxException, SwapiValidationException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/0"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
        Assert.assertEquals("Nenhum planeta encontrado", exchangeBusca.getBody().getDescription());
    }

    @Test
    public void buscarPlanetaPorIdComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }

    @Test
    public void deletarPlanetaPeloIdComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas/99"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }

    @Test
    public void deletarTodosOsPlanetasComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Não existem planetas para deletar", exchange.getBody().getDescription());
    }

}
