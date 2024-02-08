package be.abis.sandwich.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ApiError {
    private HttpStatus status;
    private String message;
//    @JsonProperty("invalid-params")
//    private List<ValidationError> invalidParams = new ArrayList<ValidationError>();
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = "[APIERROR] " + message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

//    public List<ValidationError> getInvalidParams() {
//        return this.invalidParams;
//    }

//    public void setInvalidParams(List<ValidationError> errors) {
//        this.invalidParams = errors;
//    }
}
