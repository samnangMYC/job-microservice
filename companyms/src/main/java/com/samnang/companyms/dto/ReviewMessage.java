package com.samnang.companyms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMessage {
    private Long id;
    private String reviewerName;
    private String comment;
    private Integer rating;
    private Long companyId;
}
