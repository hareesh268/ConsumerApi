package com.optum.ds.service;


import com.optum.ds.kafkavo.TinValidationResponse;

public interface TinValidateService {

	public void processTin(TinValidationResponse tinValidation)throws Exception;
}
