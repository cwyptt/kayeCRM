package io.github.cwyptt.crm.service;

import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.enums.CustomerStatus;
import io.github.cwyptt.crm.repository.CompanyRepository;
import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import io.github.cwyptt.crm.utility.EntityDtoConverter;
import io.github.cwyptt.crm.utility.exception.CompanyNotFoundException;
import io.github.cwyptt.crm.utility.exception.ContactNotFoundException;
import io.github.cwyptt.crm.utility.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final EntityDtoConverter converter;

    /**
     * Creates a new customer after validating that the email doesn't already exist.
     * The process follows these steps:
     * 1.
     * 2.
     * 3.
     * 4.
     */
    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.debug("Creating new customer relationship for contact ID: {}",
                customerDto.getContactId());

        // Fetch the contact that will become a customer
        Contact contact = contactRepository.findById(customerDto.getContactId())
                .orElseThrow(() -> new ContactNotFoundException(customerDto.getContactId()));

        // Create the customer relationship
        Customer customer = new Customer();
        customer.setContact(contact);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCustomerSince(LocalDateTime.now());

        // Set company from contact automatically
        if (contact.getCompany() != null) {
            customer.setCompany(contact.getCompany());
            customer.setCompanyName(contact.getCompany().getName());
        }

        // Other Dto fields
        customer.setStatus(customerDto.getStatus());
        customer.setNotes(customerDto.getNotes());
        customer.setAccountNumber(customerDto.getAccountNumber());
        customer.setPaymentTerms(customerDto.getPaymentTerms());
        customer.setBillingPreferences(customerDto.getBillingPreferences());

//        // If company is specified, set up company relationship
//        if (customerDto.getCompanyId() != null) {
//            Company company = companyRepository.findById(customerDto.getCompanyId())
//                    .orElseThrow(() -> new CompanyNotFoundException(customerDto.getCompanyId()));
//            customer.setCompany(company);
//        }

        // Update contact to indicate they're now a customer
        contact.setCustomer(true);
        contactRepository.save(contact);

        // Save the customer relationship
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Created new customer with ID: {}", savedCustomer.getId());

        return converter.convertCustomer(savedCustomer);
    }

    /**
     * Updates an existing customer after performing necessary validations:
     * 1. Verify customer exists
     * 2. Check if email change would conflict with existing email
     * 3. Update and save the entity
     * 4. Convert updated entity back to DTO
     */
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        log.debug("Updating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Get the contact and their company
        Contact contact = contactRepository.findById(customerDto.getContactId())
                .orElseThrow(() -> new ContactNotFoundException(customerDto.getContactId()));

        // Update company based on contact's company
        if (contact.getCompany() != null) {
            customer.setCompany(contact.getCompany());
            customer.setCompanyName(contact.getCompany().getName());
        } else {
            customer.setCompany(null);
            customer.setCompanyName(null);
        }

//        // Handle company change if needed
//        if (!Objects.equals(customer.getCompany() != null ? customer.getCompany().getId() : null,
//                customerDto.getCompanyId())) {
//            Company newCompany = customerDto.getCompanyId() != null ?
//                    companyRepository.findById(customerDto.getCompanyId())
//                            .orElseThrow(() -> new CompanyNotFoundException(customerDto.getCompanyId())) : null;
//            customer.setCompany(newCompany);
//        }

        // Update allowed fields
        customer.setContact(contact);
        customer.setStatus(customerDto.getStatus());
        customer.setNotes(customerDto.getNotes());
        customer.setAccountNumber(customerDto.getAccountNumber());
        customer.setPaymentTerms(customerDto.getPaymentTerms());
        customer.setBillingPreferences(customerDto.getBillingPreferences());

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Updated customer with ID: {}", id);

        return converter.convertCustomer(updatedCustomer);
    }

    public void deactivateCustomer(Long id) {
        log.debug("Deactivating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);

        log.info("Deactivated customer with ID: {}", id);
    }

    public CustomerDto getCustomer(Long id) {
        log.debug("Retrieving customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        log.debug("Found customer with ID: {}", id);
        return converter.convertCustomer(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        log.debug("Retrieving all customers");
        List<CustomerDto> customers = customerRepository.findAll().stream()
                .map(converter::convertCustomer)
                .collect(Collectors.toList());

        log.debug("Retrieved {} customers", customers.size());
        return customers;
    }

    public void deleteCustomer(Long id) {
        log.debug("Attempting to delete customer with ID: {}", id);

        // First verify the customer exists
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Update the associated contact to no longer be marked as a customer
        Contact contact = customer.getContact();
        contact.setCustomer(false);
        contactRepository.save(contact);

        // Now delete the customer record
        customerRepository.deleteById(id);

        log.info("Successfully deleted customer with ID: {} and updated contact status", id);
    }

    public List<CustomerDto> searchCustomers(
            CustomerStatus status,
            Long companyId,
            Long contactId,
            LocalDateTime since) {
        log.debug("Searching customers with parameters - status: {}, companyId: {}, contactId: {}, since: {}",
                status, companyId, contactId, since);

        // Validate companyId if provided
        if (companyId != null && !companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        // Validate contactId if provided
        if (contactId != null && !contactRepository.existsById(contactId)) {
            throw new ContactNotFoundException(contactId);
        }

        // If all parameters are null, return all customers
        if (status == null && companyId == null && contactId == null && since == null) {
            log.debug("No search parameters provided, returning all customers");
            return getAllCustomers();
        }

        List<CustomerDto> results = customerRepository.searchCustomers(status, companyId, contactId, since)
                .stream()
                .map(converter::convertCustomer)
                .collect(Collectors.toList());

        log.debug("Found {} customers matching search criteria", results.size());
        return results;
    }

    // Additional helper methods
    public boolean hasActiveCustomerRelationship(Long contactId) {
        log.debug("Checking if contact ID: {} has an active customer relationship", contactId);
        return customerRepository.findByContactId(contactId).stream()
                .anyMatch(customer -> customer.getStatus() == CustomerStatus.ACTIVE);
    }

    public List<CustomerDto> getCustomersByStatus(CustomerStatus status) {
        log.debug("Retrieving customers with status: {}", status);
        return customerRepository.findByStatus(status).stream()
                .map(converter::convertCustomer)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersByCompany(Long companyId) {
        log.debug("Retrieving customers for company ID: {}", companyId);
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        return customerRepository.findByCompanyId(companyId).stream()
                .map(converter::convertCustomer)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersCreatedSince(LocalDateTime since) {
        log.debug("Retrieving customers created since: {}", since);
        return customerRepository.findByCreatedAtGreaterThanEqual(since).stream()
                .map(converter::convertCustomer)
                .collect(Collectors.toList());
    }
}
