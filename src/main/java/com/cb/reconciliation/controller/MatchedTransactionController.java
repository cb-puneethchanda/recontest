package com.cb.reconciliation.controller;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.*;
import com.cb.reconciliation.persistence.Job;
import com.cb.reconciliation.persistence.JobRepository;
import com.cb.reconciliation.service.ChargebeeConnect;
import com.cb.reconciliation.service.ConvertToJSONSimple;
import com.cb.reconciliation.service.JobService;
import com.cb.reconciliation.service.MismatchedTransactions;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
public class MatchedTransactionController {

    String xeroTenantId = "e39e9d62-44ae-4814-adbc-3b897c9b67bd";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    // XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NTAwMzgxMzgsImV4cCI6MTY1MDAzOTkzOCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY1MDAzODEyNSwieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjY4MjE3N2NlMzNhZTQ0MTM4MTc4NmM5OTJkNTkwNDVjIiwianRpIjoiYzU0MjdmM2ZhMjNiMmFlNzA5ZmZiZWE1NGU1ZWUxZWMiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjM5MjI5ZWFhLWYxMzYtNDM5OS05Y2NmLTc5OGM0NDUxMjVjYSIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInNzbyJdfQ.eMJh0D6OcHaMb27NwUI-kvpFrPfaVktSLrFEXxyEP8SzbR8CXuQc5gW519GaxFXrTg3PqCq3PRb-uyjh9n275DQn3hx6f6MIJEAURqsTsSSxbut61iy6HGr50o0OLJhomgTKZCZSAimR38ZE4S_NytfvYsDhkx_5m5_Hhi-wbz7vle5W0rI-ogBC3ZkPJyltT4gx-S2Uh5fxvL_tXHyA-SSFqVjKz17nwumfxFzoBpAbOL5E_mD10StMq4Pfgw_Wv7U9Mq4MQ3MBsaHQNLmdrJCAQiBtXKorUAZQxhnyzBJVuUqFD7Tg-o5-ab51hT9OWvzoH6RljCJdDoEIOl3tGg";
    XeroCredentials xeroCredentials = new XeroCredentials(xeroTenantId, accessToken);

    String chargebeeSiteUrl = "reconciletest-test";
    String chargebeeApiKey = "test_rBsnVbkoMt0ecuqSQlRfH1xYqe3qXBqrJ";
    ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials(chargebeeSiteUrl, chargebeeApiKey);

    String stripeApiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";
    StripeCredentials stripeCredentials = new StripeCredentials(stripeApiKey);

    Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
    Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();

    @GetMapping("reconciliation/mismatched")
    public JSONObject getMismatched(@RequestParam("start") String start, @RequestParam("end") String end) throws Exception {
        Timestamp startTime = new Timestamp(Long.parseLong(start) *1000);
        Timestamp endTime = new Timestamp(Long.parseLong(end) * 1000);

        MismatchedTransactions computer = new MismatchedTransactions();

        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);

        List<Transaction> finalList = computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startTime, endTime);
        return ConvertToJSONSimple.transactions(finalList);
    }

    @Autowired
    JobService jobService;

    @Autowired
    JobRepository repository;

    @GetMapping("/job/status/{id}")
    public JSONObject getJobStatus(@PathVariable("id") String jobId) throws JSONException {
        System.out.println("GET /job/" + jobId + "/status");
//        System.out.println(jobService.getJobById(jobId));
        return jobService.getJobStatus(jobId);
    }

    @GetMapping("/job/{id}")
    public JSONObject getJob(@PathVariable("id") String jobId) throws JSONException, ParseException {
        System.out.println("GET /job/" + jobId);
//        System.out.println(jobService.getJob(jobId));
        return jobService.getJob(jobId);
    }

    @PostMapping("/job/")
    public JSONObject getAllJob(@RequestBody JobFilter jobFilter) throws JSONException, ParseException {
        System.out.println("GET /job/");
        return jobService.getAllJob(jobFilter);
    }

    @PostMapping("/reconcile")
    public JSONObject createJob(@RequestBody JobArguments arguments) throws Exception {
        System.out.println("POST /reconcile");

        String jobId = UUID.randomUUID().toString();
        Timestamp startTime = new Timestamp(Long.parseLong(arguments.getStart()) *1000);
        Timestamp endTime = new Timestamp(Long.parseLong(arguments.getEnd()) *1000);
        String gateway = arguments.getGateway();
        String siteUrl = arguments.getSiteUrl();

        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        String status = "PROCESSING";

        Job newJob = Job.builder()
                .jobId(jobId)
                .chargebeeSiteUrl(siteUrl)
                .gateway(gateway)
                .startTime(startTime)
                .endTime(endTime)
                .status(status)
                .createdAt(createdAt)
                .build();
//        System.out.println(newJob);
        repository.save(newJob);

        // credentials
        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);

        jobService.executeJob(jobId, chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap);

        JSONObject response = new JSONObject();
        response.put("jobId", jobId);
        return response;
    }

    @GetMapping("/chb_txns")
    public List<Transaction> getCbhTxns(@RequestBody JobArguments arguments) throws Exception {
        System.out.println("POST /chb_txns");

        String jobId = UUID.randomUUID().toString();
        Timestamp startTime = new Timestamp(Long.parseLong(arguments.getStart()) *1000);
        Timestamp endTime = new Timestamp(Long.parseLong(arguments.getEnd()) *1000);
        String gateway = arguments.getGateway();
        String siteUrl = arguments.getSiteUrl();

        ChargebeeConnect chargebeeConnect = new ChargebeeConnect();
        List<Transaction> chargebeeTransactions = chargebeeConnect.getTransactionsByGateway(
                chargebeeCredentials,
                GatewayEnum.STRIPE,
                startTime,
                endTime);

        return chargebeeTransactions;
    }

    @GetMapping("/site_url")
    public String getsiteUrl() throws Exception {
        System.out.println("Get /getsiteUrl");
        int pos=chargebeeSiteUrl.lastIndexOf("-");
        if(pos==-1){
            return "Error";
        }
        return chargebeeSiteUrl.substring(0,pos);
    }
}
