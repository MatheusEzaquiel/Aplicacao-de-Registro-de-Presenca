package com.mbe.ada.utils;

import com.mbe.ada.model.auth.dto.ResponseDTO;

public interface DefaultRestMethods<T> {
	
	ResponseDTO list();
	ResponseDTO save(T data);
	ResponseDTO update(Long id);
	ResponseDTO delete(Long id);

}
