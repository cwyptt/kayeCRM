// src/main/io.github.cwyptt.crm/controller/view/ContactViewController.java
package io.github.cwyptt.crm.controller.view;

import io.github.cwyptt.crm.dto.CompanyDto;
import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.service.CompanyService;
import io.github.cwyptt.crm.service.ContactService;
import io.github.cwyptt.crm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactViewController {
    private final ContactService contactService;
    private final CompanyService companyService;
    private final CustomerService customerService;

    @GetMapping
    public String listContacts(Model model) {
        model.addAttribute("activeTab", "contacts");
        model.addAttribute("pageTitle", "Contacts");
        model.addAttribute("contacts", contactService.getAllContacts());
        return "contacts/index";
    }

    @GetMapping("/{id}")
    public String viewContact(@PathVariable Long id, Model model) {
        ContactDto contact = contactService.getContact(id);
        CompanyDto company = null;
        if (contact.getCompanyId() != null) {
            company = companyService.getCompany(contact.getCompanyId());
        }

        model.addAttribute("activeTab", "contacts");
        model.addAttribute("pageTitle", "View Contact");
        model.addAttribute("contact", contact);
        model.addAttribute("company", company);
        model.addAttribute("isCustomer", customerService.hasActiveCustomerRelationship(id));

        return "contacts/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("activeTab", "contacts");
        model.addAttribute("pageTitle", "Add Contact");
        model.addAttribute("contact", new ContactDto());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "contacts/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("activeTab", "contacts");
        model.addAttribute("pageTitle", "Edit Contact");
        model.addAttribute("contact", contactService.getContact(id));
        model.addAttribute("companies", companyService.getAllCompanies());
        return "contacts/form";
    }

    @PostMapping("/save")
    public String saveContact(
            @Valid @ModelAttribute("contact") ContactDto contactDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getAllCompanies());
            return "contacts/form";
        }

        try {
            contactService.createContact(contactDto);
            redirectAttributes.addFlashAttribute("successMessage", "Contact created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating contact: " + e.getMessage());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "contacts/form";
        }

        return "redirect:/contacts";
    }

    @PostMapping("/update")
    public String updateContact(
            @Valid @ModelAttribute("contact") ContactDto contactDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getAllCompanies());
            return "contacts/form";
        }

        try {
            contactService.updateContact(contactDto.getId(), contactDto);
            redirectAttributes.addFlashAttribute("successMessage", "Contact updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating contact: " + e.getMessage());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "contacts/form";
        }

        return "redirect:/contacts";
    }
}