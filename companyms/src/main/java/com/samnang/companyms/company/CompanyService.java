package com.samnang.companyms.company;

import com.samnang.companyms.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {

    Company createCompany(Company company);

    List<Company> getAllCompanies();

    Company getCompanyById(Long id);

    Company updateCompany(Long id, Company company);

    void deleteCompany(Long id);

    void updateCompany(ReviewMessage reviewMessage);
}
