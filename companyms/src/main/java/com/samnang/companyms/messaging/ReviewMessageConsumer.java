package com.samnang.companyms.messaging;

import com.samnang.companyms.company.CompanyService;
import com.samnang.companyms.dto.ReviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RequiredArgsConstructor
public class ReviewMessageConsumer {
    private final CompanyService companyService;

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage){
        companyService.updateCompany(reviewMessage);
    }
}
