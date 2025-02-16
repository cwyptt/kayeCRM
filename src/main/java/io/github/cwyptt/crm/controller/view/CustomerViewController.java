// src/main/io.github.cwyptt.crm/controller/view/CustomerViewController.java
package io.github.cwyptt.crm.controller.view;

import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.service.ContactService;
import io.github.cwyptt.crm.service.CustomerService;
import io.github.cwyptt.crm.utility.NameUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerViewController {

    private final CustomerService customerService;
    private final ContactService contactService;

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
        List<ContactDto> contacts = contactService.getContactsByCustomer(id);
        ContactDto primaryContact = customerService.getPrimaryContact(id);

        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "View Customer");
        model.addAttribute("customer", customer);
        model.addAttribute("contacts", contacts);
        model.addAttribute("primaryContact", primaryContact);

        return "customers/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "Add Customer");
        model.addAttribute("customer", new CustomerDto());
        return "customers/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("activeTab", "customers");
        model.addAttribute("pageTitle", "Edit Customer");
        model.addAttribute("customer", customerService.getCustomer(id));
        return "customers/form";
    }

    @PostMapping("/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") CustomerDto customerDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "customers/form";
        }

        try {
            customerService.createCustomer(customerDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Customer created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating customer: " + e.getMessage());
        }

        return "redirect:/customers";
    }

    @PostMapping("/update")
    public String updateCustomer(@Valid @ModelAttribute("customer") CustomerDto customerDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "customers/form";
        }

        try {
            customerService.updateCustomer(customerDTO.getId(), customerDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating customer: " + e.getMessage());
        }

        return "redirect:/customers";
    }
}