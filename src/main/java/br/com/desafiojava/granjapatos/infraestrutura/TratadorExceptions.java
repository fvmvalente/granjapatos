package br.com.desafiojava.granjapatos.infraestrutura;

import br.com.desafiojava.granjapatos.dtos.ExceptionDTO;
import br.com.desafiojava.granjapatos.exceptions.ClienteNotFoundException;
import br.com.desafiojava.granjapatos.exceptions.PatoNotFoundException;
import br.com.desafiojava.granjapatos.exceptions.VendaNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorExceptions {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarException404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    private ResponseEntity<RestErrorMessage> clientNotFoundHandler(ClienteNotFoundException exception){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(PatoNotFoundException.class)
    private ResponseEntity<RestErrorMessage> patoNotFoundHandler(PatoNotFoundException exception){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(VendaNotFoundException.class)
    private ResponseEntity<RestErrorMessage> vendaNotFoundException(VendaNotFoundException exception){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralException(Exception exception){
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
