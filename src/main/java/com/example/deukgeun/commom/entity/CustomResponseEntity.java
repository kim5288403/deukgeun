package com.example.deukgeun.commom.entity;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponseEntity extends ResponseEntity {

    public CustomResponseEntity(HttpStatus status) {
        super(status);
    }
}
