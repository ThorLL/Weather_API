package com.weatherapi.app.weatherapi.Exeptions;
/**
 * Checked exception, thrown when an error occurs that must should be relaid onto the final HTTP response
 */
public class HttpErrorException extends Exception{
    private final int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param errorMessage Error message for the super class Exception to handle
     * @param statusCode The HTTP error type associated with this error
     */
    public HttpErrorException(String errorMessage, int statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;
    }
}
