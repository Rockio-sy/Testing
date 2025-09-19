package org.academo.academo.Exception;

import org.springframework.http.HttpStatus;

public class AuthException extends CustomException{

    public AuthException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED, "WARN");
    }
}
