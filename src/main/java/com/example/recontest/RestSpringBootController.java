package com.example.recontest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chargebee.*;
import com.chargebee.models.*;
import com.chargebee.models.enums.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.net.RequestOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestSpringBootController {

	
	@RequestMapping("/hello")
	public String hello() {
		return("Hello !");
	}
	
	@GetMapping(value="/cb_txns")
	public String getCountries(){

		Environment.configure("puneethintern-test","test_PaDmUSQGN1Z0dRCkpdBZ1DLQPMf7jvZw");
		ListResult result;
		try {
			result = Transaction.list().request();
			for(ListResult.Entry entry:result) {
				Transaction transaction = entry.transaction();
				String id_At_gateway = transaction.idAtGateway();
				System.out.println(id_At_gateway);
				
			}
			    return result.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return ("hh");
	}
	
	@GetMapping(value="/str_payments")
	public String getPaymentIntents(){


		try {
			Stripe.apiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";

			Map<String, Object> params = new HashMap<>();
			params.put("limit", 3);

			PaymentIntentCollection paymentIntents =
			  PaymentIntent.list(params);
			
			return paymentIntents.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return ("hh");
	}
	
    @GetMapping(value="/xero")
    public String connect() {
    	

        
        HttpHeaders headers = new HttpHeaders();
        headers.set("xero-tenant-id", "tenent id");
        headers.set("Authorization", "Bearer access_token.");
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String uri = "https://api.xero.com/api.xro/2.0/Invoices";

        RestTemplate restTemplate = new RestTemplate();
        
        System.out.println("GET " + uri);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

    }
	
}