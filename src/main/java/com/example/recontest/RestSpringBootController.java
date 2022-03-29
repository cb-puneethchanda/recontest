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
import com.example.recontest.models.ParseTransaction;
import com.example.recontest.models.Transactions;
import com.example.recontest.models.XeroCredentials;
import com.example.recontest.models.XeroTransaction;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
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
		List<Transactions> lis = new ArrayList<>();
		try {
			result = Transaction.list().limit(10).request();
			for(ListResult.Entry entry:result) {
				Transaction transaction = entry.transaction();
				String id_At_gateway = transaction.idAtGateway();
				String id = transaction.id();
				String type = transaction.type().toString();
				Transactions txn = new Transactions(id, id_At_gateway, type);
				lis.add(txn);
				//id_at_gatewya
				//txn_id
				//type  - refund- success
			}
			System.out.print(lis);
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
        headers.set("xero-tenant-id", "9b3db3d6-ef3e-4335-b9ff-4edf5d34b19c");
        headers.set("Authorization", "Bearer ");
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String uri = "https://api.xero.com/api.xro/2.0/Invoices";

        RestTemplate restTemplate = new RestTemplate();
        
        System.out.println("GET " + uri);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();

    }
    
    public List<Transactions> cb_txn(){
		Environment.configure("puneethintern-test","test_PaDmUSQGN1Z0dRCkpdBZ1DLQPMf7jvZw");
		ListResult result;
		List<Transactions> lis = new ArrayList<>();
		try {
			result = Transaction.list().limit(10).request();
			for(ListResult.Entry entry:result) {
				Transaction transaction = entry.transaction();
				String id_At_gateway = transaction.idAtGateway();
				String id = transaction.id();
				String type = transaction.type().toString();
				Transactions txn = new Transactions(id, id_At_gateway, type);
				lis.add(txn);
				//id_at_gatewya
				//txn_id
				//type  - refund- success
			}
//			System.out.print(lis);
			    return lis;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lis;
    }
    
    public List<Transactions> check_str(List<Transactions> lis){
    	
    	Stripe.apiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";

    	try {
    		
    		for(Transactions k : lis) {
    			String id = k.getPg_id();
    			Charge charge =
    					  Charge.retrieve(id);
    			System.out.println("Charge " + id + ":" + charge.getStatus().toString());
    			
    		}

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return lis;
    	
    }
    
    public Boolean check_str_id(Transactions k){
    	
    	Stripe.apiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";

    	try {
    		
    			String id = k.getPg_id();
    			Charge charge =
    					  Charge.retrieve(id);
    			System.out.println("Charge " + id + ":" + charge.getStatus().toString());
    			
    			if(charge!=null) {
    				return true;
    			}

		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
    	
    	return false;
    	
    }
    
	@GetMapping("/reconcile_str")
	public String reconcile() {
		List<Transactions> lis_txn = cb_txn();
		HashMap<String,List<Boolean>> lis = new HashMap<String,List<Boolean>>();
		for(Transactions txn: lis_txn) {
			lis.put(txn.getTxn_id(),Arrays.asList(reconcile_xero_id(txn),check_str_id(txn)));
		}
		
//		check_str(lis);
//		reconcile_xero(lis);
		System.out.println(lis);
		return("Done");
	}
	
	
//	@GetMapping("/reconcile_xero")
	public Boolean reconcile_xero_id(Transactions k) {
	    String access_token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg1NTM5MjIsImV4cCI6MTY0ODU1NTcyMiwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODQ1ODc0MiwieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4M";
	    String xero_tenant_id = "9b3db3d6-ef3e-4335-b9ff-4edf5d34b19c";
	    XeroConnect conn = new XeroConnect();
	    XeroCredentials cred = new XeroCredentials(access_token, xero_tenant_id);
	    
			String idAtGateway = k.getPg_id();
		    String chargebeeTxnID = k.getTxn_id();
		    XeroTransaction tr = conn.getTranscation(cred, idAtGateway, chargebeeTxnID);
		    if(tr!=null) {
		    	return true;
		    }
		return false;
	}
	
//	@GetMapping("/reconcile_xero")
	public String reconcile_xero(List<Transactions> lis) {
	    String access_token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg1NTExMzEsImV4cCI6MTY0ODU1MjkzMSwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODQ1ODc0MiwieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6ImY4MDJkNzc0MWU0NTRjMTdiMTJhYTUxN2M3NWUxNWY5IiwianRpIjoiMjBhZmQzOWEzMGVmMWUyZDBhY2NmNmE2OTMwYWUzN2IiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6Ijg3YTFkZGM4LWEzZjgtNGUzMy04NGRjLTExNmVlYTc2MmE1MSIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInB3ZCJdfQ.bhVDn1tZmct_gXysbyFIgGVp3aasiFS1qPH_IoK3BaFmPEAYh12QQH8hDFTUpYpGwKQcxK01Qj1z85EA4rc0dWaDqwa8DsNJEICiavu8O8-mZa2onkXKuKhzj6u96OOomVfcmfjEcpWmB4TQQF6S4zm2cCBkmEZtcdiXNDv15eD0UwGXyATOn1hYRDoFcj-iYC7a-1OtoriZOi2ZiA-2juU-j5u1OALmZFWT-uNaR4u5RYV-wWXBI7wvwj8AMi6KUNPwkh45ig15qCnSdsVxMj5u6i39SePwMfPZ_y63iU2pRrAWcnMwbimj4g7eN92g9pOeiRLiJomPz_2_-raUrw";
	    String xero_tenant_id = "9b3db3d6-ef3e-4335-b9ff-4edf5d34b19c";
	    XeroConnect conn = new XeroConnect();
	    XeroCredentials cred = new XeroCredentials(access_token, xero_tenant_id);
	    
		for(Transactions k : lis) {
			String idAtGateway = k.getPg_id();
		    String chargebeeTxnID = k.getTxn_id();
		    XeroTransaction tr = conn.getTranscation(cred, idAtGateway, chargebeeTxnID);
		    System.out.println(chargebeeTxnID +":"+ tr.getAmount());
	   
		}

		return("Done!");
	}

	
}