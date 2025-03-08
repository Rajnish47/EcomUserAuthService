package dev.Rajnish.EComUserAuth.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message)
    {
        super(message);
    }    
}
