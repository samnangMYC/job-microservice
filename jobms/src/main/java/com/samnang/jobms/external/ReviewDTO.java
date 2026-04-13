package com.samnang.jobms.external;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;

    private String reviewerName;

    private String comment;

    private Integer rating;

    private Long companyId;
}