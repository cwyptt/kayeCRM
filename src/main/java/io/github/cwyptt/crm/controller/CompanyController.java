package io.github.cwyptt.crm.controller;

import io.github.cwyptt.crm.dto.CompanyDto;
import io.github.cwyptt.crm.service.CompanyService;
import io.github.cwyptt.crm.utility.ErrorResponse;
import io.github.cwyptt.crm.utility.exception.CompanyHasAssociatedContactsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany (@Valid @RequestBody CompanyDto companyDto) {
        return new ResponseEntity<>(companyService.createCompany(companyDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDto companyDto) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.ok().build();
        } catch (CompanyHasAssociatedContactsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("COMPANY_HAS_CONTACTS", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyDto>> searchCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String industry) {
        return ResponseEntity.ok(
                companyService.searchCompanies(name, email, phone, industry));
    }
}
