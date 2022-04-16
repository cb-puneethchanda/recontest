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

    String xeroTenantId = "11fc848b-ac5c-43eb-8c35-39043cad93e7";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    // XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NTAxMTc2MDMsImV4cCI6MTY1MDExOTQwMywiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiNThBQTQzMEZFOTc5NENGMDgyQzdEMzYzMjg1MEJENDYiLCJzdWIiOiI5MjVmYmEyZGNjZjI1MWEzOTEyMWUyZTg4M2I5OTk2NCIsImF1dGhfdGltZSI6MTY1MDExNzU4OSwieGVyb191c2VyaWQiOiI4ZTZhZDI1NS1jMDlkLTQzYmEtODY1NS0wYTcyYjY2MDRhOTciLCJnbG9iYWxfc2Vzc2lvbl9pZCI6ImVkZDQzYjg2YTdmNTQyMDg4Y2M5NzViMzNmZTIwNTc0IiwianRpIjoiNmI0NDU2MjY5MzBhM2M3ODQ2M2MzZTViNjI4NTlmN2EiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6ImRiZjkxNjAzLTM1MDQtNDk2MC1iOWU1LWQyNGM5Nzc1MjY4OSIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsImFjY291bnRpbmcuY29udGFjdHMiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.tVoR3i-1QcsWm233ySejJX3VN3f83o1buYkW8MB5SH-QM59o1hrXWcmcBGfRu_UGTkOW4CN7Zt7660c2Bzs6LrQZgmU0xsQNgrTCi9hAlzqjqFxbcYUdfXEYC8kZOTOIO6emr_hjxHY3xJKcAcba4EOEAILDLB1mBvKi0rcOQmlUa5Yr2fTiWw0AoIqpLXUiF2T_ySB0vqY8hQPgCO5koGk5SOvDS_whnoUNqlgBAbjKQergF5Vaz-fknUWMFgZOl-V_-WP0Snn3oSjccrd3pLAI4kKLbTT3f0FBCDhs3j4-NxMZ-wOx8-Qqgu9rdfPHHfTHQ-KMOogzY4bedUTxGw";
    XeroCredentials xeroCredentials = new XeroCredentials(xeroTenantId, accessToken);

    String chargebeeSiteUrl = "xaio-yan-test";
    String chargebeeApiKey = "test_aPW2LccuW8AfjCUxnUl5ZXBlC7dG4UeXG";
    ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials(chargebeeSiteUrl, chargebeeApiKey);

    String stripeApiKey = "sk_test_51Kgn4aSHg3lW4nFeGQTCtYFmp1ZGTUWIBSyuoTq0rQIxmfBScW1HLcUnnDw8I1Mae5Bo2WmLT89aHNeASzqa4OsL00Ww9J1pKt";
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
