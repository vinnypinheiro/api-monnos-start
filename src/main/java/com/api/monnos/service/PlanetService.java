package com.api.monnos.service;

import com.api.monnos.repository.PlanetRepository;
import com.api.monnos.validation.SwapiValidationException;
import com.api.monnos.model.Planet;
import com.api.monnos.model.SwapiPlanet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Slf4j
@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private SwapiPlanetService swapiPlanetService;

    @Autowired
    private CounterService CounterService;

    /**
     * Busca de todos os planetas cadastrados     *
     * @return List<Planet> planetas
     */
    public List<Planet> findAll() {
        return planetRepository.findAll();
    }

    /**
     * Busca de planeta pelo ID     *
     * @param id
     * @return Optional<Planet> planeta
     */
    public Optional<Planet> findById(String id) {
        return Optional.ofNullable(
                planetRepository.findById(
                        Long.parseLong(id)
                )
        );
    }

    /**
     * Busca de planeta pelo Nome     *
     * @param nome
     * @return
     */
    public Optional<Planet> findByNome(String nome) {
        return Optional.ofNullable(planetRepository.findByNome(nome));
    }

    /**
     * Adicionar um planeta (com nome, clima e terreno)
     * @param planet
     * @return Planet planeta
     */
    public Planet create(Planet planet) throws SwapiValidationException {

        return planetRepository.save(planet);
    }

    /**
     * Verifica se existe algum Planet criado com o nome passado pelo parametro
     * @param planetaNome
     * @return boolean
     */
    public boolean isThereAPlanetNamed(String planetaNome) {
        Optional<Planet> planetaOptional = this.findByNome(planetaNome);

        if(planetaOptional.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deleta no banco de dados o Planet recebido por parâmetro
     * @param planet
     */
    public void delete(Planet planet) {
        planetRepository.delete(planet);
    }

    /**
     * Retorna o Total de aparicoes do planeta nos filmes
     * @param nome
     * @return
     * @throws SwapiValidationException
     */
    public Integer aparicoesEmFilmesTotal(String nome) throws SwapiValidationException {

        SwapiPlanet swapiPlanet = swapiPlanetService.getPlanetInSwapiByName(nome);

        return swapiPlanet.getFilms().size();
    }

    /**
     * Deleta TODOS os planetas no banco de dados
     */
    public void deleteAll() {
        planetRepository.deleteAll();
    }
}
