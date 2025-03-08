package dev.Rajnish.EComUserAuth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.Rajnish.EComUserAuth.controller.AuthController;
import dev.Rajnish.EComUserAuth.dto.ExceptionResponseDTO;

@ControllerAdvice(basePackageClasses = AuthController.class)
public class AuthControllerExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponseDTO> invalidTokenExceptionHandler(InvalidTokenException e)
    {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();
        exceptionResponseDTO.setCode(401);
        exceptionResponseDTO.setMessage("Invalid token");
        return new ResponseEntity<>(exceptionResponseDTO,HttpStatus.UNAUTHORIZED);
    }    
}
