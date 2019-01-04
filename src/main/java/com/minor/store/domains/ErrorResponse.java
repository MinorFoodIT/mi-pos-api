package com.minor.store.domains;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.minor.store.utils.GeneratorUtility;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ErrorResponse {
	private final String id = GeneratorUtility.generateTransactionId();
	private String code;
	private String message;
	private List<ErrorV1Field> errors = new ArrayList<>();

    public void addFieldError(String param, String code, String message) {
    	ErrorV1Field error = new ErrorV1Field(code, param, message);
    	errors.add(error);
    }
	
	@Data
	public static class ErrorV1Field implements Serializable {
		private String code;
		private String param;
		private String message;

		public ErrorV1Field(String code, String param, String message) {
			this.code = code;
			this.param = param;
			this.message = message;
		}
	}
}
