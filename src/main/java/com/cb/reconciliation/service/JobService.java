package com.cb.reconciliation.service;

import com.cb.reconciliation.persistence.Job;
import com.cb.reconciliation.persistence.JobRepository;
import com.cb.reconciliation.utils.ErrorJSON;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobService {
    private JobRepository repository;

    @Autowired
    JobService(JobRepository repository) {
        this.repository = repository;
    }

    public JSONObject getJobById(String jobId) throws JSONException {
        Optional<Job> jobOptional = repository.findJobByJobId(jobId);
        if (jobOptional.isPresent()) {
            JSONObject response = new JSONObject();
            String status = jobOptional.get().getStatus();
            response.put("jobId", jobId);
            response.put("status", status);
            return response;
        }
        else {
            ErrorJSON errorJSON = new ErrorJSON("NO_JOB", "Job with id " + jobId + " doesnt exist");
            return errorJSON.toJSON();
        }
    }
}
