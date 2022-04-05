package com.api.monnos.model;

import lombok.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class SwapiPlanet {

    /**
     * A number denoting the gravity of this planet. Where 1 is normal.
     * (Required)
     *
     */
    public String gravity;
    /**
     * the terrain of this planet. Comma-seperated if diverse.
     * (Required)
     *
     */
    public String terrain;
    /**
     * The ISO 8601 date format of the time that this resource was created.
     * (Required)
     *
     */
    public Date created;
    /**
     * An array of People URL Resources that live on this planet.
     * (Required)
     *
     */
    public List<Object> residents = null;
    /**
     * The percentage of the planet surface that is naturally occuring water or bodies of water.
     * (Required)
     *
     */
    public String surface_water;
    /**
     * the ISO 8601 date format of the time that this resource was edited.
     * (Required)
     *
     */
    public Date edited;
    /**
     * An array of Film URL Resources that this planet has appeared in.
     * (Required)
     *
     */
    public List<Object> films = null;
    /**
     * The climate of this planet. Comma-seperated if diverse.
     * (Required)
     *
     */
    public String climate;
    /**
     * The name of this planet.
     * (Required)
     *
     */
    public String name;
    /**
     * The diameter of this planet in kilometers.
     * (Required)
     *
     */
    public String diameter;
    /**
     * The average populationof sentient beings inhabiting this planet.
     * (Required)
     *
     */
    public String population;
    /**
     * The number of standard hours it takes for this planet to complete a single rotation on its axis.
     * (Required)
     *
     */
    public String rotation_period;
    /**
     * The hypermedia URL of this resource.
     * (Required)
     *
     */
    public URI url;
    /**
     * The number of standard days it takes for this planet to complete a single orbit of its local star.
     * (Required)
     *
     */
    public String orbital_period;

    public SwapiPlanet() {}

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Object> getResidents() {
        return residents;
    }

    public void setResidents(List<Object> residents) {
        this.residents = residents;
    }

    public String getSurface_water() {
        return surface_water;
    }

    public void setSurface_water(String surface_water) {
        this.surface_water = surface_water;
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public List<Object> getFilms() {
        return films;
    }

    public void setFilms(List<Object> films) {
        this.films = films;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getRotation_period() {
        return rotation_period;
    }

    public void setRotation_period(String rotation_period) {
        this.rotation_period = rotation_period;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public String getOrbital_period() {
        return orbital_period;
    }

    public void setOrbital_period(String orbital_period) {
        this.orbital_period = orbital_period;
    }
}
