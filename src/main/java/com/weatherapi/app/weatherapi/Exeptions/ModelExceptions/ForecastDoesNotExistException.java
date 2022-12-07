package com.weatherapi.app.weatherapi.Exeptions.ModelExceptions;

/**
 * Checked exception, thrown when a forecast could not be found
 */
public class ForecastDoesNotExistException extends Exception{
    public ForecastDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
