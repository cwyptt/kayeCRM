package io.github.cwyptt.crm.utility;

import io.github.cwyptt.crm.dto.CompanyDto;
import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.entity.Company;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.dto.ContactDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting between entities and DTOs.
 * Provides type-safe conversion with special handling for entity-specific relationships.
 */
@Component
public class EntityDtoConverter {

    /**
     * Converts any source object to a target class using Spring's BeanUtils.
     * This method handles the basic property copying that's common to all conversions.
     */
    public <T, S> T convert(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error converting " + source.getClass().getSimpleName()
                    + " to " + targetClass.getSimpleName(), e);
        }
    }

    /**
     * Updates an existing entity with values from a DTO.
     * This method is useful when you want to update an entity while preserving
     * certain fields or relationships.
     */
    public <T, S> void update(S source, T target) {
        BeanUtils.copyProperties(source, target, "id", "createdAt");
    }

    /**
     * Converts a Company entity to a CompanyDto.
     * Handles any special mapping requirements specific to companies.
     */
    public CompanyDto convertCompany(Company company) {
        // Add any special mapping logic here if needed
        return convert(company, CompanyDto.class);
    }

    /**
     * Converts a Contact entity to a ContactDto.
     * Includes additional fields from related entities that are useful in the DTO.
     */
    public ContactDto convertContact(Contact contact) {
        ContactDto dto = convert(contact, ContactDto.class);
        if (contact.getCompany() != null) {
            dto.setCompanyId(contact.getCompany().getId());
            dto.setCompanyName(contact.getCompany().getName());
        }
        return dto;
    }

    /**
     * Converts a Customer entity to a CustomerDto.
     * Includes additional fields from related entities that are useful in the DTO.
     */
    public CustomerDto convertCustomer(Customer customer) {
        CustomerDto dto = convert(customer, CustomerDto.class);
        dto.setContactId(customer.getContact().getId());
        dto.setContactFullName(NameUtils.getFullName(customer.getContact()));

        if (customer.getCompany() != null) {
            dto.setCompanyId(customer.getCompany().getId());
            dto.setCompanyName(customer.getCompany().getName());
        }
        return dto;
    }
}