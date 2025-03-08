package dev.Rajnish.EComUserAuth.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message)
    {
        super(message);
    }    
}
