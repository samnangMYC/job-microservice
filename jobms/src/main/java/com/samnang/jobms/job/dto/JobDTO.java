package com.samnang.jobms.job.dto;

import com.samnang.jobms.external.Company;
import com.samnang.jobms.external.CompanyDTO;
import com.samnang.jobms.external.Review;
import com.samnang.jobms.external.ReviewDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class JobDTO {

    private Long id;

    private String title;

    private String description;

    private String location;

    private Double salary;

    private CompanyDTO company;

    private List<ReviewDTO> reviews;
}
