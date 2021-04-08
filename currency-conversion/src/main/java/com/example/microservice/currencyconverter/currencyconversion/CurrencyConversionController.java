package com.example.microservice.currencyconverter.currencyconversion;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy proxy; 

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		System.out.println("Test");
		HashMap<String, String> urlVariables = new HashMap<>();
		urlVariables.put("from", from);
		urlVariables.put("to", to);
		
		 ResponseEntity<CurrencyConversion> responseEntity = new
		 RestTemplate().getForEntity(
		 "http://localhost:8001/currency-exchange/from/{from}/to/{to}",
		 CurrencyConversion.class,urlVariables);
		 
		 CurrencyConversion currencyConversion = responseEntity.getBody();
		 
		 return new CurrencyConversion(currencyConversion.getId(), from , to,quantity,
		 currencyConversion.getConversionMultiple(),
		 quantity.multiply(currencyConversion.getConversionMultiple()),
		 currencyConversion.getEnvironment()+""+"Rest Template");
		 
		/*
		 * return new CurrencyConversion(1000L, from , to,quantity, BigDecimal.ONE,
		 * BigDecimal.ONE,"");
		 */

	}
	

	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		
		 
		 CurrencyConversion currencyConversion = proxy.RetreiveExchangeValue(from, to);
		 
		 return new CurrencyConversion(currencyConversion.getId(), from , to,quantity,
		 currencyConversion.getConversionMultiple(),
		 quantity.multiply(currencyConversion.getConversionMultiple()),
		 currencyConversion.getEnvironment() + "" + "Feign");
		 
		/*
		 * return new CurrencyConversion(1000L, from , to,quantity, BigDecimal.ONE,
		 * BigDecimal.ONE,"");
		 */

	}
}
