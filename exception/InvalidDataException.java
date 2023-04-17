package com.go.tokenverification.exception;

/**
 * @Overview
 *  This class is type of exception if data is invalid
 */
public class InvalidDataException extends Exception{
    public InvalidDataException(String errorMessage){
        super(errorMessage);
    }
}
