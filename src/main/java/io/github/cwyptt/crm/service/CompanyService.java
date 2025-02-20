package io.github.cwyptt.crm.service;

import io.github.cwyptt.crm.dto.CompanyDto;
import io.github.cwyptt.crm.entity.Company;
import io.github.cwyptt.crm.repository.CompanyRepository;
import io.github.cwyptt.crm.utility.EntityDtoConverter;
import io.github.cwyptt.crm.utility.ValidationUtils;
import io.github.cwyptt.crm.utility.exception.CompanyHasAssociatedContactsException;
import io.github.cwyptt.crm.utility.exception.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ValidationUtils validationUtils;
    private final EntityDtoConverter converter;

    public CompanyDto createCompany(CompanyDto companyDto) {
        log.debug("Creating new company with name: {}", companyDto.getName());

        // Validate email uniqueness
        validationUtils.verifyEmailNotExists(
                companyDto.getEmail(),
                companyRepository,
                "Company"
        );

        // Convert and save
        Company company = converter.convert(companyDto, Company.class);
        Company savedCompany = companyRepository.save(company);

        log.info("Created new company with ID: {}", savedCompany.getId());
        return converter.convertCompany(savedCompany);
    }

    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        log.debug("Updating company with ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        // Validate email update
        validationUtils.verifyEmailUpdate(
                company,
                companyDto.getEmail(),
                companyRepository,
                "Company"
        );

        // Update fields but preserve relationships
        converter.update(companyDto, company);
        Company updatedCompany = companyRepository.save(company);

        log.info("Updated company with ID: {}", id);
        return converter.convertCompany(updatedCompany);
    }

    public CompanyDto getCompany(Long id) {
        return companyRepository.findById(id)
                .map(converter::convertCompany)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(converter::convertCompany)
                .collect(Collectors.toList());
    }

    public List<CompanyDto> searchCompanies(
            String name,
            String email,
            String phone,
            String industry) {
        return companyRepository
                .searchCompanies(name, email, phone, industry)
                .stream()
                .map(converter::convertCompany)
                .collect(Collectors.toList());
    }

    public void deleteCompany(Long id) {
        log.debug("Attempting to delete company with ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        // Check if company has any contacts or customers
        if (!company.getContacts().isEmpty()) {
            throw new CompanyHasAssociatedContactsException(id);
        }

        companyRepository.deleteById(id);
        log.info("Deleted company with ID: {}", id);
    }
}
