package io.github.cwyptt.crm.service;

import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import io.github.cwyptt.crm.utility.EntityDtoConverter;
import io.github.cwyptt.crm.utility.exception.ContactNotFoundException;
import io.github.cwyptt.crm.utility.exception.CustomerNotFoundException;
import io.github.cwyptt.crm.utility.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;

    public ContactDto createContact(ContactDto contactDto) {
        Customer customer = customerRepository.findById(contactDto.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        if (contactRepository.existsByEmailAndCustomerId(contactDto.getEmail(), contactDto.getCustomerId())) {
            throw new EmailAlreadyExistsException(contactDto.getCustomerId());
        }

        Contact contact = new Contact();
        BeanUtils.copyProperties(contactDto, contact);
        contact.setCustomer(customer);

        Contact savedContact = contactRepository.save(contact);
        return EntityDtoConverter.convertContact(savedContact);
    }

    public ContactDto updateContact(Long id, ContactDto contactDto) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(ContactNotFoundException::new);

        // Check if email is being changed and if new email already exists
        if (!contact.getEmail().equals(contactDto.getEmail()) &&
                contactRepository.existsByEmailAndCustomerId(contactDto.getEmail(), contact.getCustomer().getId())) {
            throw new EmailAlreadyExistsException("Email already exists for this customer");
        }

        // If customer is being changed, verify the new customer exits
        if (!contact.getCustomer().getId().equals(contactDto.getCustomerId())) {
            Customer newCustomer = customerRepository.findById(contactDto.getCustomerId())
                    .orElseThrow(CustomerNotFoundException::new);
            contact.setCustomer(newCustomer);
        }

        BeanUtils.copyProperties(contactDto, contact, "id", "customer", "createdAt");
        Contact updatedContact = contactRepository.save(contact);

        return EntityDtoConverter.convertContact(updatedContact);

    }

    public ContactDto getContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(ContactNotFoundException::new);
        return EntityDtoConverter.convertContact(contact);
    }

    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(EntityDtoConverter::convertContact)
                .collect(Collectors.toList());
    }

    public List<ContactDto> getContactsByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return contactRepository.findByCustomerId(customerId)
                .stream()
                .map(EntityDtoConverter::convertContact)
                .collect(Collectors.toList());
    }

    public List<ContactDto> searchContacts(
            String firstName, String lastName,
            String email, String phone,
            String position, String department,
            Long customerId
    ) {
        return contactRepository.searchContacts(firstName, lastName, email, phone, position, department, customerId)
                .stream()
                .map(EntityDtoConverter::convertContact)
                .collect(Collectors.toList());
    }

    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException(id);
        }
        contactRepository.deleteById(id);
    }
}
