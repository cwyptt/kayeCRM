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

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerViewController {
    private final CustomerService customerService;
    private final ContactService contactService;
    private final CompanyService companyService;

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "Customers");
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/index";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable Long id, Model model) {
        CustomerDto customer = customerService.getCustomer(id);
        ContactDto contact = contactService.getContact(customer.getContactId());
        CompanyDto company = null;
        if (customer.getCompanyId() != null) {
            company = companyService.getCompany(customer.getCompanyId());
        }

        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "View Customer");
        model.addAttribute("customer", customer);
        model.addAttribute("contact", contact);
        model.addAttribute("company", company);

        return "customers/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "Add Customer");
        model.addAttribute("customer", new CustomerDto());
        model.addAttribute("contacts", contactService.getAllContacts());
        return "customers/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "Edit Customer");
        model.addAttribute("customer", customerService.getCustomer(id));
        model.addAttribute("contacts", contactService.getAllContacts());
        return "customers/form";
    }

    @PostMapping("/save")
    public String saveCustomer(
            @Valid @ModelAttribute("customer") CustomerDto customerDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            prepareFormModel(model);
            return "customers/form";
        }

        try {
            customerService.createCustomer(customerDto);
            redirectAttributes.addFlashAttribute("successMessage", "Customer created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating customer: " + e.getMessage());
            prepareFormModel(model);
            return "customers/form";
        }

        return "redirect:/customers";
    }

    @PostMapping("/update")
    public String updateCustomer(
            @Valid @ModelAttribute("customer") CustomerDto customerDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            prepareFormModel(model);
            return "customers/form";
        }

        try {
            customerService.updateCustomer(customerDto.getId(), customerDto);
            redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating customer: " + e.getMessage());
            prepareFormModel(model);
            return "customers/form";
        }

        return "redirect:/customers";
    }

    private void prepareFormModel(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        model.addAttribute("companies", companyService.getAllCompanies());
    }
}