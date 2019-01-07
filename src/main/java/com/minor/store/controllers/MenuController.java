package com.minor.store.controllers;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.minor.store.domains.BasicResponse;
import com.minor.store.domains.reponse.QueryMenuDetailResponse;

@RestController
public class MenuController {
	
	//DaoException

	@GetMapping("/api/v1/menu/{menu_id}")
	@ApiOperation(value = "Query menu profile", tags = "Menu", notes = "{query-menu_profile}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Request validation fail"),
			@ApiResponse(code = 400, message = "Menu not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<BasicResponse<QueryMenuDetailResponse>> queryMenuDetail(
			@PathVariable("menu_id") String menuId) throws  InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidParameterSpecException, InvalidKeySpecException, IOException {

		if (!StringUtils.isNumeric(menuId) || menuId.length() > 32) {
			//throw new MerchantIdlValidatorException(merchantId);
		}

		BasicResponse<QueryMenuDetailResponse> responseData = new BasicResponse<>(new QueryMenuDetailResponse());
		responseData.setData(null);
		
		return null;
	}
}
