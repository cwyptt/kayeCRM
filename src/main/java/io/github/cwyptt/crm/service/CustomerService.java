package io.github.cwyptt.crm.service;

import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import io.github.cwyptt.crm.utility.EntityDtoConverter;
import io.github.cwyptt.crm.utility.ValidationUtils;
import io.github.cwyptt.crm.utility.exception.CustomerHasAssociatedContactsException;
import io.github.cwyptt.crm.utility.exception.CustomerNotFoundException;
import io.github.cwyptt.crm.utility.exception.EmailAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final ValidationUtils validationUtils;

    public CustomerService(
            CustomerRepository customerRepository,
            ContactRepository contactRepository,
            ValidationUtils validationUtils
            ) {
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.validationUtils = validationUtils;
    }

    /**
     * Creates a new customer after validating that the email doesn't already exist.
     * The process follows these steps:
     * 1. Check if email is unique
     * 2. Convert DTO to entity
     * 3. Save the entity
     * 4. Convert saved entity back to DTO
     */
    public CustomerDto createCustomer(CustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        // Convert Dto to entity, save it, and convert it back to Dto
        Customer customer = EntityDtoConverter.convert(customerDto, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return EntityDtoConverter.convertCustomer(savedCustomer);
    }

    /**
     * Updates an existing customer after performing necessary validations:
     * 1. Verify customer exists
     * 2. Check if email change would conflict with existing email
     * 3. Update and save the entity
     * 4. Convert updated entity back to DTO
     */
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Check if email is being changed and if new email already exists
        if (!customer.getEmail().equals(customerDto.getEmail()) && customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        // Copy properties, excluding id to prevent overwrite
        EntityDtoConverter.convert(customerDto, customer.getClass());
        customer.setId(id);  // Ensure ID remains unchanged

        Customer updatedCustomer = customerRepository.save(customer);
        return EntityDtoConverter.convertCustomer(updatedCustomer);
    }

    public CustomerDto getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return EntityDtoConverter.convertCustomer(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(EntityDtoConverter::convertCustomer)
                .collect(Collectors.toList());
    }

    public void deleteCustomer(Long id) {
        validationUtils.verifyCustomerExists(id);
        validationUtils.verifyCustomerHasContact(id);

        customerRepository.deleteById(id);
    }

    public List<CustomerDto> searchCustomers(
            String firstName,
            String lastName,
            String email,
            String phone,
            String company) {

        // If all parameters are null, return all customers
        if (firstName == null && lastName == null &&
            email == null && phone == null && company == null) {
            return getAllCustomers();
        }

        return customerRepository.searchCustomers(firstName, lastName, email, phone, company)
                .stream()
                .map(EntityDtoConverter::convertCustomer)
                .collect(Collectors.toList());
    }

    // Extra functions
    public ContactDto getPrimaryContact(Long customerId) {
        logger.debug("Retrieving primary contact for customer ID: {}", customerId);

        validationUtils.verifyCustomerExists(customerId);

        // Find all contacts for the customer and get the primary one
        return contactRepository.findByCustomerId(customerId).stream()
                .filter(Contact::isPrimary)
                .findFirst()
                .map(EntityDtoConverter::convertContact)
                .orElse(null);
    }

    public boolean hasPrimaryContact(Long customerId) {
        logger.debug("Checking for a primary contact of customer ID: {}", customerId);

        validationUtils.verifyCustomerExists(customerId);

        return contactRepository.findByCustomerId(customerId).stream()
                .anyMatch(Contact::isPrimary);
    }
}
