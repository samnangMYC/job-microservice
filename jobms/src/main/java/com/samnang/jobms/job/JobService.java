package com.samnang.jobms.job;

import com.samnang.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {
    Job createJob(Job job);

    List<JobDTO> getAllJobs();

    Job getJobById(Long id);

    Job updateJob(Long id, Job job);

    void deleteJob(Long id);
}
