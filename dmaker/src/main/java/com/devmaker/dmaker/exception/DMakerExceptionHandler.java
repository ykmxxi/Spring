package com.devmaker.dmaker.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devmaker.dmaker.dto.DMakerErrorResponse;

import lombok.extern.slf4j.Slf4j;

// @RestControllerAdvice: 각 컨트롤러에 Advice 를 주는 클래스
@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(DMakerException.class)
	public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request) {
		log.error("errorCode: {}, url: {}, message: {}",
			e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

		return DMakerErrorResponse.builder()
			.errorCode(e.getDMakerErrorCode())
			.errorMessage(e.getDetailMessage())
			.build();
	}

	// 컨트롤러에 진입하기 전 발생하는 예외들을 처리
	@ExceptionHandler(value = {
		HttpRequestMethodNotSupportedException.class,
		MethodArgumentNotValidException.class
	})
	public DMakerErrorResponse handleBadRequest(Exception e, HttpServletRequest request) {
		log.error("url: {}, message: {}",
			request.getRequestURI(), e.getMessage());

		return DMakerErrorResponse.builder()
			.errorCode(DMakerErrorCode.INVALID_REQUEST)
			.errorMessage(DMakerErrorCode.INVALID_REQUEST.getMessage())
			.build();
	}

	// 위 예외처리 메서드로 처리되지 않는 알수없는 오류들
	@ExceptionHandler(Exception.class)
	public DMakerErrorResponse handleException(Exception e, HttpServletRequest request) {
		log.error("url: {}, message: {}",
			request.getRequestURI(), e.getMessage());

		return DMakerErrorResponse.builder()
			.errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
			.errorMessage(DMakerErrorCode.INTERNAL_SERVER_ERROR.getMessage())
			.build();
	}
}
