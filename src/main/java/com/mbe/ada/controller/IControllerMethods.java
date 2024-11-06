package com.mbe.ada.controller;

import org.springframework.http.ResponseEntity;

public interface IControllerMethods {
	
	<T> ResponseEntity<T> index();
	<T> ResponseEntity create(T data);
	<T> ResponseEntity<T> get(Long id);
	<T> ResponseEntity<T> update(Long id);
	<T> ResponseEntity<T> delete(Long id);

}
