package com.minor.store.domains.reponse;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class QueryMenuDetailResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
}
