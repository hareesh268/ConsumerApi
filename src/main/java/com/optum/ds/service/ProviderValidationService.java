package com.optum.ds.service;


import com.optum.ds.kafkavo.PESResponse;

public interface ProviderValidationService {

	public void providerValidation(PESResponse pesResponse)throws Exception;
}
