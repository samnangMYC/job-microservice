package com.samnang.companyms.company.impl;

import com.samnang.companyms.clients.ReviewClient;
import com.samnang.companyms.company.Company;
import com.samnang.companyms.company.CompanyRepository;
import com.samnang.companyms.company.CompanyService;
import com.samnang.companyms.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ReviewClient reviewClient;

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Override
    public Company updateCompany(Long id, Company request) {

        Company company = getCompanyById(id);

        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());
        company.setDescription(request.getDescription());

        return companyRepository.save(company);
    }

    @Override
    public void deleteCompany(Long id) {

        Company company = getCompanyById(id);

        companyRepository.delete(company);
    }

    @Override
    public void updateCompany(ReviewMessage reviewMessage) {
        System.out.println(reviewMessage.getComment());

        Company company = companyRepository.findById(reviewMessage.getId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        double averageRating = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRepository.save(company);
    }


}
