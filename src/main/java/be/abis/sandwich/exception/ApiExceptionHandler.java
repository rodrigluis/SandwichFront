package be.abis.sandwich.exception;

import be.abis.sandwich.model.ApiError;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    protected ResponseEntity<? extends Object> handleApiException(ApiException aex, WebRequest request) {
        ApiError err = new ApiError(aex.getHttpStatus(), "[RESTAPI]" + aex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        return new ResponseEntity<ApiError>(err, responseHeaders, aex.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                MethodArgumentNotValidException ex,
                HttpHeaders headers,
                HttpStatusCode status,
                WebRequest request) {
        ApiError err = new ApiError((HttpStatus)status, ex.getMessage());
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        List<ValidationError> validationErrorList = err.getInvalidParams();
//        for (FieldError fe : fieldErrors) {
//            ValidationError validationError = new ValidationError();
//            validationError.setName(fe.getField());
//            validationError.setReason(fe.getDefaultMessage());
//            validationErrorList.add(validationError);
//        }
        return new ResponseEntity<Object>(err, headers, status);
    }
}
