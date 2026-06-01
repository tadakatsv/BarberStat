package ua.chekmaryov.barber_stat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.chekmaryov.barber_stat.exception.AlreadyExistsException;
import ua.chekmaryov.barber_stat.exception.BadRequestException;
import ua.chekmaryov.barber_stat.exception.ResourceNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail handleAlreadyExistsApiException(AlreadyExistsException exception) {
        log.warn("Already exists! {}" , exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundApiException(ResourceNotFoundException exception) {
        log.warn("ResourceNotFoundException! {}" , exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestApiException(BadRequestException exception) {
        log.warn("BadRequestException! {}" , exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception exception) {
        log.error("Internal Server Error! Critical error!: {}" , exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error, check logs");
    }
}