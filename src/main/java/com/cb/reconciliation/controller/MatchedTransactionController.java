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
import org.json.JSONString;
import org.json.simple.JSONArray;
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
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NTAzNTg1NzEsImV4cCI6MTY1MDM2MDM3MSwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiNThBQTQzMEZFOTc5NENGMDgyQzdEMzYzMjg1MEJENDYiLCJzdWIiOiI5MjVmYmEyZGNjZjI1MWEzOTEyMWUyZTg4M2I5OTk2NCIsImF1dGhfdGltZSI6MTY1MDM1ODU2MSwieGVyb191c2VyaWQiOiI4ZTZhZDI1NS1jMDlkLTQzYmEtODY1NS0wYTcyYjY2MDRhOTciLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjVmZWNmNzUzNDhlZDQxMmU4MmU4ZTI1ZjA5N2NjNWFmIiwianRpIjoiM2EyMDQxMGZlMTFlMmJkYWI0MWE4MjRmODgwNjAwMWQiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6ImI3MmY3MmMyLWEzYzMtNGEzMy05YjA5LTdkM2M5NTA1NDA0ZCIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsImFjY291bnRpbmcuY29udGFjdHMiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsic3NvIl19.Wy2_AssG7qNmW03w_Adez3rGcPRgaD8wf-fsVrgS_AOXG9rtKMEqcHckNy_V8h7cLGKI-VBNRNky2MoAh6M6pYUR3xvpiqGoaRBdTA1t-EToPWC_AX2RkKpZMWXrFswtlPNbKStpnt1VnuQkZ1G4pH3YUDPz6ft4WdNkMzIDSsUsJsTG3K7oJoGkEk8fIWuFyMUPsovlX63xq2oE8BlPLN5AGilWccAPo_1c3K7ej5vL--_swXO_jV5p8vLP88pP76bbgj1vrN1FCVsyDe411UoYLCHLMriHNhWcP9eRSnCUXw83QpsFwQK2PzQnY0slVKPaz9IMF07lXHWJpCltxg";
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
