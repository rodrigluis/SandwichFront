package be.abis.sandwich.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private Type type;

    public ApiException(Type type) {
        super(type.getMessage());
        this.type = type;
    }

    public HttpStatus getHttpStatus() {
        return this.type.getStatus();
    }

    public String getMessage() {
        return super.getMessage();
    }

    public enum Type {
        NO_SANDWICH(HttpStatus.NO_CONTENT, "There is no sandwich"),
        ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "The sandwich already exists"),
        DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "The sandwich does not exist");

        private HttpStatus status;
        private String message;

        private Type(HttpStatus status, String message) {
            this.status  = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return this.status;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
