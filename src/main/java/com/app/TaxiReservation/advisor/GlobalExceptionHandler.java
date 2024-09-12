package com.app.TaxiReservation.advisor;

import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({SQLException.class})
    public ResponseEntity<ResponseUtil> sqlException(SQLException e) {
        e.printStackTrace();
        return new ResponseEntity<>(
                new ResponseUtil(404, "SQL Exception", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserNotExistException.class})
    public ResponseEntity<ResponseUtil> userNotFound(UserNotExistException e) {
        e.printStackTrace();
        return new ResponseEntity<>(
                new ResponseUtil(404, "User Not Found Exception", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

}
