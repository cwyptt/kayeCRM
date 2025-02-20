package io.github.cwyptt.crm.service;

import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.entity.Company;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.repository.CompanyRepository;
import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import io.github.cwyptt.crm.utility.EntityDtoConverter;
import io.github.cwyptt.crm.utility.ValidationUtils;
import io.github.cwyptt.crm.utility.exception.CompanyNotFoundException;
import io.github.cwyptt.crm.utility.exception.ContactHasCustomerRelationshipException;
import io.github.cwyptt.crm.utility.exception.ContactNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final ValidationUtils validationUtils;
    private final EntityDtoConverter converter;
    private final CustomerRepository customerRepository;

    public ContactDto createContact(ContactDto contactDto) {
        log.debug("Creating new contact: {} {}",
                contactDto.getFirstName(),
                contactDto.getLastName());

        // Validate email uniqueness
        validationUtils.verifyEmailNotExists(
                contactDto.getEmail(),
                contactRepository,
                "Contact"
        );

        // Create the contact entity
        Contact contact = converter.convert(contactDto, Contact.class);

        // If a company is specified, fetch and set it
        if (contactDto.getCompanyId() != null) {
            Company company = companyRepository.findById(contactDto.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(contactDto.getCompanyId()));
            contact.setCompany(company);

            // If this is the first contact for the company, make them primary
            if (contactRepository.findByCompanyId(company.getId()).isEmpty()) {
                contact.setPrimaryContact(true);
            }
        }

        Contact savedContact = contactRepository.save(contact);
        log.info("Created new contact with ID: {}", savedContact.getId());

        return converter.convertContact(savedContact);
    }

    public ContactDto updateContact(Long id, ContactDto contactDto) {
        log.debug("Updating contact with ID: {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));

        // Verify email update won't conflict
        validationUtils.verifyEmailUpdate(
                contact,
                contactDto.getEmail(),
                contactRepository,
                "Contact"
        );

        // Handle company change if needed
        if (!Objects.equals(contact.getCompany().getId(), contactDto.getCompanyId())) {
            Company newCompany = companyRepository.findById(contactDto.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(contactDto.getCompanyId()));

            // If this contact was primary for old company, find new primary
            if (contact.isPrimaryContact()) {
                handlePrimaryContactChange(contact.getCompany().getId());
            }

            contact.setCompany(newCompany);
        }

        // Update the contact's fields
        converter.update(contactDto, contact);
        Contact updatedContact = contactRepository.save(contact);

        log.info("Updated contact with ID: {}", id);
        return converter.convertContact(updatedContact);
    }

    public void deleteContact(Long id) {
        log.debug("Attempting to delete contact with ID: {}", id);

        List<Customer> associatedCustomers = customerRepository.findByContactId(id);
        if (!associatedCustomers.isEmpty()) {
            throw new ContactHasCustomerRelationshipException(id);
        }

        if(!contactRepository.existsById(id)) {
            throw new ContactNotFoundException(id);
        }

        contactRepository.deleteById(id);
        log.info("Successfully deleted contact with ID: {}", id);
    }

    public ContactDto getContact(Long id) {
        return contactRepository.findById(id)
                .map(converter::convertContact)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public List<ContactDto> getContactsByCompany(Long companyId) {
        return contactRepository.findByCompanyId(companyId).stream()
                .map(converter::convertContact)
                .collect(Collectors.toList());
    }

    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(converter::convertContact)
                .collect(Collectors.toList());
    }

    private void handlePrimaryContactChange(Long companyId) {
        // Find another contact to make primary
        contactRepository.findByCompanyId(companyId).stream()
                .filter(c -> !c.isPrimaryContact())
                .findFirst()
                .ifPresent(newPrimary -> {
                    newPrimary.setPrimaryContact(true);
                    contactRepository.save(newPrimary);
                });
    }

    public List<ContactDto> searchContacts(
            String firstName, String lastName,
            String email, String phone,
            String position, String department,
            Long companyId, Boolean isCustomer
    ) {
        return contactRepository.searchContacts(firstName, lastName, email, phone, position, department, companyId, isCustomer)
                .stream()
                .map(converter::convertContact)
                .collect(Collectors.toList());
    }
}
