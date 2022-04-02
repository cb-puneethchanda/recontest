package com.cb.reconciliation.service;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.AccSoftCredentials;
import com.cb.reconciliation.model.credentials.ChargebeeCredentials;
import com.cb.reconciliation.model.credentials.GatewayCredentials;
import com.cb.reconciliation.persistence.Job;
import com.cb.reconciliation.persistence.JobRepository;
import com.cb.reconciliation.utils.JSONFormatConverter;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class JobService {
    private JobRepository repository;

    @Autowired
    private MismatchedTransactions computer;

    @Autowired
    JobService(JobRepository repository) {
        this.repository = repository;
    }

    public JSONObject getJobStatus(String jobId) throws JSONException {
        Optional<Job> jobOptional = repository.findJobByJobId(jobId);
        if (!jobOptional.isPresent()) {
            return Job.errorJSON(jobId);
        }
        JSONObject response = new JSONObject();
        String status = jobOptional.get().getStatus();
        response.put("jobId", jobId);
        response.put("status", status);
        return response;
    }

    public JSONObject getJob(String jobId) throws JSONException, ParseException {
        Optional<Job> jobOptional = repository.findJobByJobId(jobId);
        if (!jobOptional.isPresent()) {
            // todo status code
            return Job.errorJSON(jobId);
        }
        Job job = jobOptional.get();
        JSONObject response = JSONFormatConverter.toSimpleJSON(job.getMismatched());
        return response;
    }

    public JSONObject getAllJob(JobFilter jobFilter) {
        String chargebeeSiteUrl = jobFilter.getSiteUrl();
        List<String> jobIds = repository.findJobIdByChargebeeSiteUrl(chargebeeSiteUrl);
        JSONObject response = new JSONObject();
        JSONArray jobIdJSON = new JSONArray();

        for (String jobId: jobIds) {
            jobIdJSON.add(jobId);
        }
        response.put("jobIds", jobIdJSON);
        return response;

    }

    @Async
    public void executeJob(
            String jobId,
            ChargebeeCredentials chargebeeCredentials,
            Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap,
            Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap
            ) throws Exception {
        Job job = repository.getById(jobId);

        List<Transaction> finalList = computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, job.getStartTime(), job.getEndTime());
        org.json.JSONObject result = ConvertToJSON.transactions(finalList);

        job.setMismatched(result);
//        Thread.sleep(1000);

        job.setStatus("SUCCESS");
        System.out.println("JOB " + jobId + " COMPLETED!");
    }
}
