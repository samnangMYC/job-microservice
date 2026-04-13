package com.samnang.jobms.job.impl;

import com.samnang.jobms.clients.CompanyClient;
import com.samnang.jobms.clients.ReviewClient;
import com.samnang.jobms.external.Company;
import com.samnang.jobms.external.CompanyDTO;
import com.samnang.jobms.external.Review;
import com.samnang.jobms.external.ReviewDTO;
import com.samnang.jobms.job.Job;
import com.samnang.jobms.job.JobRepository;
import com.samnang.jobms.job.JobService;
import com.samnang.jobms.job.dto.JobDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    @Override
    public Job createJob(Job job) {
       return jobRepository.save(job);
    }


    @Override
    public List<JobDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
                .map(job ->{

                    // call company-service
                    CompanyDTO company = null;
                    try {
                        company = companyClient.getCompany(job.getCompanyId());
                    } catch (Exception e) {
                        // log warning
                        log.warn("Company fetch failed: {}", job.getCompanyId(), e);
                    }

                    // call review
                    List<ReviewDTO> reviews =  List.of();
                    try {
                        reviews = reviewClient.getReviews(job.getCompanyId());
                    } catch (Exception e){
                        log.warn("Review fetch failed: {}", job.getCompanyId(), e);
                    }

                    return JobDTO.builder()
                            .id(job.getId())
                            .title(job.getTitle())
                            .description(job.getDescription())
                            .location(job.getLocation())
                            .salary(job.getSalary())
                            .company(company)
                            .reviews(reviews)
                            .build();
                }).toList();

    }
    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    @Override
    public Job updateJob(Long id, Job request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyId(request.getCompanyId());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        jobRepository.delete(job);
    }

}
