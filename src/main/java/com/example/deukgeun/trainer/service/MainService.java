package com.example.deukgeun.trainer.service;

import org.springframework.http.ResponseEntity;

public interface MainService {
	ResponseEntity<?> getList(String keyword);
}
