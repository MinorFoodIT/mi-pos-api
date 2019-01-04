package com.minor.store.utils;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import com.minor.store.constant.Constants;

public class GeneratorUtility {
	private GeneratorUtility () {}
	
	public static String generateTransactionId() {
		// Pick from ThreadContxt
		String transactionId = ThreadContext.get(Constants.THREAD_CONTEXT_TRANSACTION_ID);
		// If null or empty, generate new one
		if (StringUtils.isEmpty(transactionId)) {
			return convertString(UUID.randomUUID().toString());
		} else {
			return convertString(transactionId);
		}
	}

	private static String convertString(String transactionId){
		String[] transactionSplited = transactionId.split("-");
        if(transactionSplited.length > 2){
            transactionId = "sapi-"+ transactionId.replace("-", "");
        }else if(transactionSplited.length == 1){
            transactionId = "sapi-"+ transactionSplited[0];
        }
        return transactionId;
	}
}

