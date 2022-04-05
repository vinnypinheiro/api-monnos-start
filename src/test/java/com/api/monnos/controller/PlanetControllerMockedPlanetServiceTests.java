package com.api.monnos.controller;

import com.api.monnos.repository.PlanetRepository;
import com.api.monnos.service.PlanetService;
import com.api.monnos.validation.SwapiValidationException;
import com.api.monnos.PlanetApplication;
import com.api.monnos.commons.PlanetResponseBody;
import com.api.monnos.commons.Utility;
import com.api.monnos.model.Planet;
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
public class PlanetControllerMockedPlanetServiceTests {

    @Autowired
    PlanetRepository planetRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PlanetService planetServiceMocked;

    @Before
    public void init() {
        planetRepository.deleteAll();
    }

    @Test
    public void cadastrarPlanetaComErroPorDuplicidade() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                planetServiceMocked.isThereAPlanetNamed(Mockito.any())
        ).thenReturn(true);

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
        Assert.assertEquals(HttpStatus.CONFLICT, exchange.getStatusCode());
        Assert.assertEquals("Planet já existente.", exchange.getBody().getDescription());
    }

    @Test
    public void cadastrarPlanetaComErroInterno() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                planetServiceMocked.isThereAPlanetNamed(Mockito.any())
        ).thenReturn(false);

        Mockito.when(
                planetServiceMocked.create(Mockito.any())
        ).thenThrow(new RuntimeException());

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
    public void listarPlanetasComErroInterno() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                planetServiceMocked.findAll()
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void buscarPlanetaPorNomeComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetServiceMocked.findByNome(Mockito.any())
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome=Tatooine"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void buscarPlanetaPorIdComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetServiceMocked.findByNome(Mockito.any())
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void deletarPlanetaPorIdComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetServiceMocked.findById(Mockito.any())
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void deletarTodosOsPlanetasComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetServiceMocked.findAll()
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetResponseBody> exchange = restTemplate
                .exchange(entity, PlanetResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

}
