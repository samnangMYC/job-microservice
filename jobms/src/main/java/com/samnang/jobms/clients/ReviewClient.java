package com.samnang.jobms.clients;


import com.samnang.jobms.external.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "REVIEW-SERVICE")
public interface ReviewClient {

    @GetMapping("/reviews")
    List<ReviewDTO> getReviews(@RequestParam("companyId") Long companyId);

}
