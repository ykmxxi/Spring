package com.devmaker.dmaker.dto;

import com.devmaker.dmaker.exception.DMakerErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DMakerErrorResponse {
	private DMakerErrorCode errorCode;
	private String errorMessage;
}
