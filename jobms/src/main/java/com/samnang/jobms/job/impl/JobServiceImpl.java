package com.samnang.jobms.job.impl;

import com.samnang.jobms.clients.CompanyClient;
import com.samnang.jobms.clients.ReviewClient;
import com.samnang.jobms.external.CompanyDTO;
import com.samnang.jobms.external.ReviewDTO;
import com.samnang.jobms.job.Job;
import com.samnang.jobms.job.JobRepository;
import com.samnang.jobms.job.JobService;
import com.samnang.jobms.job.dto.JobDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    int attempt = 0;

    @Override
    public Job createJob(Job job) {
       return jobRepository.save(job);
    }

    @Override
    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> getAllJobs() {
        System.out.println("Attempt: " + attempt++);

        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
                .map(job -> {
                    CompanyDTO company = null;
                    try {
                        company = companyClient.getCompany(job.getCompanyId());
                    } catch (Exception e) {
                        log.warn("Company fetch failed: {}", job.getCompanyId(), e);
                    }

                    List<ReviewDTO> reviews = List.of();
                    try {
                        reviews = reviewClient.getReviews(job.getCompanyId());
                    } catch (Exception e) {
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
                })
                .toList();
    }

    public List<JobDTO> companyBreakerFallback(Throwable t) {
        log.warn("Fallback triggered: {}", t.getMessage());
        return List.of();
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
