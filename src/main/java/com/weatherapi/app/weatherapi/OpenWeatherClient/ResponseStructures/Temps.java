package com.weatherapi.app.weatherapi.OpenWeatherClient.ResponseStructures;
/**
 * Class representing the json datastructures given from OpenWeatherMap
 * Used to extract information from the OpenWeatherMap api json response
 */
public class Temps {
    private double temp;
    private double temp_min;
    private double temp_max;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
}
