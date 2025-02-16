package io.github.cwyptt.crm.utility;

import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.utility.exception.DtoConvertionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Utility class for converting between entities and DTOs.
 * Provides type-safe conversion with special handling for entity-specific relationships.
 */
public class EntityDtoConverter {
    private static final Logger logger = LoggerFactory.getLogger(EntityDtoConverter.class);

    /**
     * Converts any source object to a target class instance using BeanUtils.
     * This is the base conversion method that handles standard property copying.
     *
     * @param source The source object to convert from
     * @param targetClass The class to convert to
     * @param <T> The type of the target class
     * @param <S> The type of the source object
     * @return An instance of the target class with properties copied from source
     */
    public static <T, S> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            logger.warn("Attempted to convert null source object to {}", targetClass.getSimpleName());
            throw new IllegalArgumentException("Source object cannot be null");
        }
        try {
            logger.debug("Converting {} to {}", source.getClass().getSimpleName(), targetClass.getSimpleName());

            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);

            logger.debug("Successfully converted {} to {}",
                    source.getClass().getSimpleName(), targetClass.getSimpleName());
            return target;
        } catch (Exception e) {
            logger.error("Error converting {} to {}: {}",
                    source.getClass().getSimpleName(), targetClass.getSimpleName(), e.getMessage());
            throw new DtoConvertionFailedException(source.getClass().getSimpleName());
        }
    }

    /**
     * Specialized conversion method for Contact entities.
     * Handles the specific relationships and derived properties of Contacts.
     *
     * @param contact The Contact entity to convert
     * @return A ContactDTO with all properties properly set
     */
    public static ContactDto convertContact(Contact contact) {
        logger.debug("Converting Contact entity with ID: {}", contact.getId());
        ContactDto dto = convert(contact, ContactDto.class);

        logger.debug("Contact's full name: {}", NameUtils.getFullName(contact));

        if (contact.getCustomer() != null) {
            dto.setCustomerId(contact.getCustomer().getId());
            dto.setPrimary(contact.isPrimary());
            dto.setCustomerCompany(contact.getCustomer().getCompany());
            logger.debug("Added customer-specific fields for Contact ID: {}", contact.getId());
        }
        return dto;
    }

    /**
     * Specialized conversion method for Customer entities.
     * Can be extended to handle any customer-specific conversions.
     *
     * @param customer The Customer entity to convert
     * @return A CustomerDto with all properties properly set
     */
    public static CustomerDto convertCustomer(Customer customer) {
        logger.debug("Converting Customer entity with ID: {}", customer.getId());
        CustomerDto dto = convert(customer, CustomerDto.class);

        logger.debug("Customer's full name: {}", NameUtils.getFullName(customer));
        return dto;
    }
}