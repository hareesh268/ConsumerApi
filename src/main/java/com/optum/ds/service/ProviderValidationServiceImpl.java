package com.optum.ds.service;

import com.optum.ds.entity.MailServiceEntity;
import com.optum.ds.entity.PaaSecurityKeyEntity;
import com.optum.ds.entity.UhcProviderEntity;
import com.optum.ds.exception.DSError;
import com.optum.ds.kafkavo.CorpOwnerLetterGeneration;
import com.optum.ds.kafkavo.PESResponse;
import com.optum.ds.repository.MailServiceEntityRepository;
import com.optum.ds.repository.PaaSecurityKeyEntityRepository;
import com.optum.ds.repository.UhcProviderRepository;
import com.optum.ds.repository.UserEntityRepository;
import com.optum.ds.util.CommonUtil;
import com.optum.ds.util.Constants;
import com.optum.ds.util.ErrorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderValidationServiceImpl implements ProviderValidationService {

    @Autowired
    private UhcProviderRepository uhcProviderRepository;

    @Autowired
    private PaaSecurityKeyEntityRepository paaSecurityKeyEntityRepository;

    @Autowired
    private MailServiceEntityRepository mailServiceEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderValidationServiceImpl.class.getCanonicalName());

    @Override
    public void providerValidation(PESResponse pesResponse){

        CorpOwnerLetterGeneration pesData = pesResponse.getCorpOwnerLetterGenerationList().get(0);
        LOGGER.warn("This is the prov_id from PES {}", pesData.getProvId());

        String mpin = String.format("%9s", pesData.getProvId()).replace(' ', '0');


        if (StringUtils.isBlank(pesData.getMktCityNm())) {
            // non par provider do nothing at this point
            LOGGER.warn("Non par provider from PES with prov_id: {} and MPIN: {}", pesData.getProvId(), mpin);
            return;
        }

        //we are setting MPIN instead of TIN
        long count = mailServiceEntityRepository.countByTinAndType(mpin, Constants.MAIL_KEY_TYPE);
        if (count > 0) {
            // already have a record for this tin in mail service entity. do nothing for now
            LOGGER.warn("Mpin already exists in mail service entity for prov_id: {} and MPIN: {}", pesData.getProvId(), mpin);
            return;
        }

        List<UhcProviderEntity> uhcProviderEntityList = uhcProviderRepository.findByMpin(mpin);

        if (CollectionUtils.isEmpty(uhcProviderEntityList)) {

            LOGGER.warn("Mpin not found in DS {}", mpin);
            //creating new uhcProviderId
            String uhcProviderId = CommonUtil.generateUhcProviderID(mpin);

            UhcProviderEntity newProvider = new UhcProviderEntity();
            newProvider.setMpin(mpin);
            newProvider.setUhcProviderId(uhcProviderId);
            newProvider.setFirstName(pesData.getProFstNm());
            newProvider.setLastName(pesData.getProLstNm());
            uhcProviderRepository.insert(newProvider);

            // this is to generate security key and add document into mailService
            generateKeyAndMail(pesData, uhcProviderId, mpin);

        } else {
            LOGGER.warn("Mpin found in DS {}", mpin);
            List<String> uhcProviderIds = uhcProviderEntityList.stream().map(t -> t.getUhcProviderId()).collect(Collectors.toList());

            //if multiple uhc-providerId then we can throw error .
            if (uhcProviderIds.size() > 1) {
                LOGGER.error("GenerateKeyServiceImpl : Multiple uhcProviderIds for MPIN {}", mpin);
                throw new DSError(ErrorUtil.ERROR_CODE_LETTER_GENERATION_MULTIPLE_UHC_PROVIDERS_FOUND, ErrorUtil.ERROR_MESSAGE_LETTER_GENERATION_MULTIPLE_UHC_PROVIDERS_FOUND, HttpStatus.BAD_REQUEST);
            }

            Long userCount = userEntityRepository.countByUhcProviderId(uhcProviderIds.get(0));

            if (userCount == 0) {
                // If the MPIN does exist in DS with no users -> send provider info in Registration letter to Shutterfly
                // this is to generate security key and add document into mailService
                LOGGER.warn("No Users found for uhcProviderId {}", uhcProviderIds.get(0));
                generateKeyAndMail(pesData, uhcProviderIds.get(0), mpin);

            }
        }
    }

    private void generateKeyAndMail(CorpOwnerLetterGeneration pesData , String uhcProviderId, String mpin){
        //creating new paaSecurity document
        String key = (CommonUtil.generateSecurityKey()).toUpperCase();
        String orgName = CommonUtil.setOrgName(pesData.getProFstNm(), pesData.getProLstNm());

        PaaSecurityKeyEntity paaSecurityKeyEntity = new PaaSecurityKeyEntity();
        paaSecurityKeyEntity.setUhcProviderId(uhcProviderId);
        paaSecurityKeyEntity.setSecurityKey(key);
        paaSecurityKeyEntity.setStatus(Constants.KEY_PENDING);
        paaSecurityKeyEntity.setCorpName(orgName);
        paaSecurityKeyEntityRepository.insert(paaSecurityKeyEntity);

        //creating new mailService document
        MailServiceEntity mailServiceEntity = new MailServiceEntity();
        mailServiceEntity.setType(Constants.MAIL_KEY_TYPE);
        mailServiceEntity.setSecurityKey(key);

        //setting mpin in place of tin
        mailServiceEntity.setTin(mpin);

        mailServiceEntity.setApprovalOrgName(orgName);
        mailServiceEntity.setApprovalFirstName(pesData.getProFstNm());
        mailServiceEntity.setApprovalLastName(pesData.getProLstNm());
        mailServiceEntity.setApprovalAddressLine1(pesData.getAdrAdrLn1Txt());
        mailServiceEntity.setApprovalAddressLine2("");
        mailServiceEntity.setApprovalCity(pesData.getAdrCtyNm());
        mailServiceEntity.setApprovalState(pesData.getAdrStCd());
        mailServiceEntity.setApprovalZipCode(pesData.getAdrZipCd());
        mailServiceEntityRepository.insert(mailServiceEntity);
    }
}
