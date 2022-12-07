package com.weatherapi.app.weatherapi.Models;

/**
 * @param temp Average temperature
 * @param temp_min Minimum temperature
 * @param temp_max Maximum temperature
 */
public record Forecast(double temp, double temp_min, double temp_max) {

}
