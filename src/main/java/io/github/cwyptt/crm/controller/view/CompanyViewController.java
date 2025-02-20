package io.github.cwyptt.crm.controller.view;

import io.github.cwyptt.crm.dto.CompanyDto;
import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.service.CompanyService;
import io.github.cwyptt.crm.service.ContactService;
import io.github.cwyptt.crm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/companies")
@Slf4j
public class CompanyViewController {
    private final CompanyService companyService;
    private final ContactService contactService;
    private final CustomerService customerService;

    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("activeTab", "companies");
        model.addAttribute("pageTitle", "Companies");
        model.addAttribute("companies", companyService.getAllCompanies());
        return "companies/index";
    }

    @GetMapping("/{id}")
    public String viewCompany(@PathVariable Long id, Model model) {
        CompanyDto company = companyService.getCompany(id);
        List<ContactDto> contacts = contactService.getContactsByCompany(id);
        List<CustomerDto> customers = customerService.getCustomersByCompany(id);

        model.addAttribute("activeTab", "companies");
        model.addAttribute("pageTitle", "View Company");
        model.addAttribute("company", company);
        model.addAttribute("contacts", contacts);
        model.addAttribute("customers", customers);

        return "companies/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("activeTab", "companies");
        model.addAttribute("pageTitle", "Add Company");
        model.addAttribute("company", new CompanyDto());
        return "companies/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("activeTab", "companies");
        model.addAttribute("pageTitle", "Edit Company");
        model.addAttribute("company", companyService.getCompany(id));
        return "companies/form";
    }

    @PostMapping("/save")
    public String saveCompany(
            @Valid @ModelAttribute("company") CompanyDto companyDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "companies/form";
        }

        try {
            companyService.createCompany(companyDto);
            redirectAttributes.addFlashAttribute("successMessage", "Company created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating company: " + e.getMessage());
            return "companies/form";
        }

        return "redirect:/companies";
    }

    @PostMapping("/update")
    public String updateCompany(
            @Valid @ModelAttribute("company") CompanyDto companyDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "companies/form";
        }

        try {
            companyService.updateCompany(companyDto.getId(), companyDto);
            redirectAttributes.addFlashAttribute("successMessage", "Company updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating company: " + e.getMessage());
            return "companies/form";
        }

        return "redirect:/companies";
    }



}