package com.api.monnos.controller;

import com.api.monnos.service.PlanetService;
import com.api.monnos.validation.SwapiValidationException;
import com.api.monnos.commons.PlanetResponseBody;
import com.api.monnos.commons.PlanetRequestBody;
import com.api.monnos.model.Planet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Slf4j
@RestController
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    /**
     * Endpoint para adicionar/criar um planeta (com nome, clima e terreno)
     * @param planetaRequest
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.POST)
    public HttpEntity<PlanetResponseBody> create(@Valid @RequestBody PlanetRequestBody planetaRequest,
                                                 BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                log.error("Erros na requisicao do cliente: {}", bindingResult.toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(bindingResult));
            }

            String planetaNome = "";

            if(!planetaRequest.getPlanet().getNome().isEmpty()) {
                planetaNome = planetaRequest.getPlanet().getNome();
            }

            if(planetService.isThereAPlanetNamed(planetaNome)) {

                PlanetResponseBody responseBody = PlanetResponseBody.builder().build();
                responseBody.setDescription("Planeta já existente.");

                log.error("Nao é permitido criar planeta com um nome já existente.");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);

            } else {

                Planet planet = planetService.create(planetaRequest.getPlanet());

                PlanetResponseBody responseBody = PlanetResponseBody.builder()
                        .planet(planet)
                        .build();

                log.info("Planeta adicionado com sucesso:  [{}]", planet);

                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            }

        } catch (SwapiValidationException swapiException) {

            String errorCode = String.valueOf(System.nanoTime());

            PlanetResponseBody responseBody = PlanetResponseBody.builder().build();

            if(swapiException.getHttpStatus() == HttpStatus.NOT_FOUND) {
                responseBody.setDescription(
                        "Não é possível adicionar um planeta inexistente na API Star Wars"
                );
            } else {
                responseBody.setDescription(
                        "Houve um erro na API Star Wars ao consultar a existência do planeta para cadastro"
                );
            }

            log.error("Erro na SWAPI ao cadastrar o planeta: {} - {}",
                    errorCode, swapiException.getMessage(), swapiException);

            return ResponseEntity.status(swapiException.getHttpStatus()).body(responseBody);

        } catch (Exception e) {

            String errorCode = String.valueOf(System.nanoTime());

            PlanetResponseBody responseBody = PlanetResponseBody.builder().build();
            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro no cadastro do planeta: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para listar os planetas cadastrados
     * @return HttpEntity<PlanetResponseBody> planeta
     */
    @RequestMapping(path = "/planets", method = RequestMethod.GET)
    public HttpEntity<PlanetResponseBody> findAll() {

        PlanetResponseBody responseBody = PlanetResponseBody.builder().build();

        try {

            List<Planet> planets = planetService.findAll();

            if (!planets.isEmpty()) {

                responseBody.setPlanets(planets);
                log.info("Busca de planetas com exito");
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            } else {

                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro ao listar os planetas cadastrados: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para buscar um planeta pelo nome
     * @param nome
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planets/", method = RequestMethod.GET)
    public HttpEntity<PlanetResponseBody> findByName(@RequestParam("nome") String nome) {

        PlanetResponseBody responseBody = PlanetResponseBody.builder().build();

        try {

            Optional<Planet> planetaOptional = planetService.findByNome(nome);

            if (planetaOptional.isPresent()) {
                Planet planet = planetaOptional.get();

                responseBody.setPlanet(planet);

                log.info("Busca de planeta pelo nome com exito: [{}]", planet);

                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            } else {
                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado pelo o nome: {}", nome);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro na exibição do Planeta solicitado com o nome: {} - {} - {}", nome, errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para buscar um planeta pelo id
     * @param id
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas/{id}", method = RequestMethod.GET)
    public HttpEntity<PlanetResponseBody> findById(@PathVariable String id) {

        PlanetResponseBody responseBody = PlanetResponseBody.builder().build();

        try {

            Optional<Planet> planetaOptional = planetService.findById(id);

            if (planetaOptional.isPresent()) {

                Planet planet = planetaOptional.get();
                responseBody.setPlanet(planet);
                log.info("Busca de planeta com exito: [{}]", planet);
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            } else {
                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado pelo id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro na exibição do Planeta solicitado com o ID: {} - {} - {}", id, errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para deletar um planeta pelo id
     * @param id
     * @return HttpEntity<PlanetResponseBody>
     */
    @RequestMapping(path = "/planetas/{id}", method = RequestMethod.DELETE)
    public HttpEntity<PlanetResponseBody> deleteById(@PathVariable String id) {

        PlanetResponseBody responseBody = PlanetResponseBody.builder().build();

        try {

            Optional<Planet> planetaOptional = planetService.findById(id);

            if (planetaOptional.isPresent()) {

                Planet planet = planetaOptional.get();
                planetService.delete(planet);
                log.info("Planeta deletado pelo id: {}", id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            } else {
                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado pelo id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {

            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro ao deletar o Planeta solicitado com o ID: {} - {} - {}", id, errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint para deletar todos os planetas
     * @return
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.DELETE)
    public HttpEntity<PlanetResponseBody> deleteAll() {

        PlanetResponseBody responseBody = PlanetResponseBody.builder().build();

        try {

            List<Planet> planetList = planetService.findAll();

            if (!planetList.isEmpty()) {

                planetService.deleteAll();
                log.info("Todos os planetas foram deletados");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            } else {
                responseBody.setDescription("Não existem planetas para deletar");

                log.error("Não existem planetas para deletar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {

            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro ao deletar todos os Planetas: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Coletando os erros de BindResult para setar ResponseBody
     * @param bindingResult
     * @return PlanetResponseBody
     */
    private PlanetResponseBody buildErrorResponse(BindingResult bindingResult) {

        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(
                        fieldError -> bindingResult.getFieldError(
                                fieldError.getField()
                        )
                        .getDefaultMessage()
                )
                .collect(Collectors.toList());

        PlanetResponseBody responseBody = PlanetResponseBody.builder().build();
        responseBody.setDescription(errors.toString());
        return responseBody;
    }

}
