package com.weatherapi.app.weatherapi.OpenWeatherClient.ResponseStructures;

import java.util.List;

/**
 * Class representing the json datastructures given from OpenWeatherMap
 * Used to extract information from the OpenWeatherMap api json response
 */
public class FiveDayForecast {
    private List<RawForecast> list;

    public List<RawForecast> getList() {
        return list;
    }

    public void setList(List<RawForecast> list) {
        this.list = list;
    }
}
