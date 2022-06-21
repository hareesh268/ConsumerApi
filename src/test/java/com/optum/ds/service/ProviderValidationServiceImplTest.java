package com.optum.ds.service;

import com.optum.ds.entity.UhcProviderEntity;
import com.optum.ds.kafkavo.CorpOwnerLetterGeneration;
import com.optum.ds.kafkavo.PESResponse;
import com.optum.ds.repository.MailServiceEntityRepository;
import com.optum.ds.repository.PaaSecurityKeyEntityRepository;
import com.optum.ds.repository.UhcProviderRepository;
import com.optum.ds.repository.UserEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProviderValidationServiceImplTest {

    @InjectMocks
    ProviderValidationServiceImpl providerValidationService;

    @Mock
    private UhcProviderRepository uhcProviderEntityRepo;

    @Mock
    private UserEntityRepository userEntityRepo;

    @Mock
    private PaaSecurityKeyEntityRepository paaSecurityKeyEntityRepository;

    @Mock
    private MailServiceEntityRepository mailServiceEntityRepository;


    @Before
    public void before(){

    }

    @Test
    public void noMpinFound() throws Exception{
        List<UhcProviderEntity> uhcProviderEntityList = new ArrayList<>();
        Mockito.when(uhcProviderEntityRepo.findByMpin(Mockito.anyString())).thenReturn(uhcProviderEntityList);
        providerValidationService.providerValidation(createPESMessage());
    }

    @Test
    public void whenMpinFoundWithNoUsers() throws Exception{
        Mockito.when(uhcProviderEntityRepo.findByMpin(Mockito.anyString())).thenReturn(returnUhcProvider());
        providerValidationService.providerValidation(createPESMessage());
    }

    @Test
    public void whenMpinFoundWithUsers() throws Exception{

        Long userCount = 2L;
        Mockito.when(uhcProviderEntityRepo.findByMpin(Mockito.anyString())).thenReturn(returnUhcProvider());
        Mockito.when(userEntityRepo.countByUhcProviderId(Mockito.anyString())).thenReturn(userCount);
        providerValidationService.providerValidation(createPESMessage());
    }

    public PESResponse createPESMessage(){
        PESResponse pesResponse = new PESResponse();
        pesResponse.setCorrelationIdentifier("correlation-id");

        CorpOwnerLetterGeneration corpOwnerLetterGeneration = new CorpOwnerLetterGeneration();
        corpOwnerLetterGeneration.setProFstNm("FirstName");
        corpOwnerLetterGeneration.setProLstNm("LastName");
        corpOwnerLetterGeneration.setProvId("MPIN");
        corpOwnerLetterGeneration.setAdrAdrLn1Txt("Adr-Line-1");
        corpOwnerLetterGeneration.setAdrCtyNm("City");
        corpOwnerLetterGeneration.setAdrStCd("St");
        corpOwnerLetterGeneration.setAdrZipCd("Zip");
        corpOwnerLetterGeneration.setMktCityNm("Hobbiton");


        List<CorpOwnerLetterGeneration> corpOwnerLetterGenerationList = new ArrayList<>();

        corpOwnerLetterGenerationList.add(corpOwnerLetterGeneration);
        pesResponse.setCorpOwnerLetterGenerationList(corpOwnerLetterGenerationList);

        return pesResponse;

    }

    public List<UhcProviderEntity> returnUhcProvider(){

        List<UhcProviderEntity> uhcProviderEntityList = new ArrayList<>();
        UhcProviderEntity uhcProviderEntity = new UhcProviderEntity();
        uhcProviderEntity.setMpin("MPIN");
        uhcProviderEntity.setFirstName("FirstName");
        uhcProviderEntity.setLastName("LastName");
        uhcProviderEntity.setUhcProviderId("uhcProviderId");

        uhcProviderEntityList.add(uhcProviderEntity);
        return uhcProviderEntityList;
    }

}
