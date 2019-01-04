package com.minor.store.domains;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class BasicResponse<T> {
	
	@JsonProperty("data")
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
	
	@JsonProperty("status")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ErrorResponse status;

	public BasicResponse(T data) {
		this.data = data;
	}

	public static BasicResponse empty() {
		return new BasicResponse(new HashMap<String, String>());
	}
}
