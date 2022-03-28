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

@RestController
public class RestSpringBootController {
	
	@RequestMapping("/hello")
	public String hello() {
		return("Hello World!");
	}
	
	@GetMapping(value="/cb_txns")
	public String getCountries(){

		Environment.configure("puneethintern-test","key");
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
			Stripe.apiKey = "key";

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
}