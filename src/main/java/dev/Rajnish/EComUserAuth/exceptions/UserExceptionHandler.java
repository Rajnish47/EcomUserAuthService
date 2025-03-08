package dev.Rajnish.EComUserAuth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.Rajnish.EComUserAuth.controller.UserController;
import dev.Rajnish.EComUserAuth.dto.ExceptionResponseDTO;

@ControllerAdvice(basePackageClasses = UserController.class)
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity userNotFoundExceptionHandler(UserNotFoundException e)
    {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setMessage(e.getMessage());
        exceptionResponseDTO.setCode(404);

        return new ResponseEntity<>(exceptionResponseDTO,HttpStatus.NOT_FOUND);
    }
    
}
