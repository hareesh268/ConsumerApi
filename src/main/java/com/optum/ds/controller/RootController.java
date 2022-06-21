package com.optum.ds.controller;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Root Controller
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = { "/" }) 
public class RootController
{     
	@RequestMapping(value="/", method = RequestMethod.GET)     
	public String healthCheck()  {
		return "Digital Security Consumer API is up and running - V1.0";

	} 
}  
