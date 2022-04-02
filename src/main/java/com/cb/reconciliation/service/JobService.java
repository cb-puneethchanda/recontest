package com.cb.reconciliation.service;

import com.cb.reconciliation.model.JobFilter;
import com.cb.reconciliation.persistence.Job;
import com.cb.reconciliation.persistence.JobRepository;
import com.cb.reconciliation.utils.JSONFormatConverter;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private JobRepository repository;

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
}
