package dev.Rajnish.EComUserAuth.exceptions.user_exceptios;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message)
    {
        super(message);
    }    
}
