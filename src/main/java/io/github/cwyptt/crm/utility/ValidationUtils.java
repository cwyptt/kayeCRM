package io.github.cwyptt.crm.utility;

import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import io.github.cwyptt.crm.utility.exception.CustomerHasAssociatedContactsException;
import io.github.cwyptt.crm.utility.exception.CustomerNotFoundException;
import io.github.cwyptt.crm.utility.exception.EmailAlreadyExistsException;
import io.github.cwyptt.crm.utility.exception.FieldNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for common validation operations.
 * Provides centralized validation logic that can be reused across services.
 */
@Component
public class ValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    // Cache for email fields to avoid repeated reflection lookups
    private final Map<Class<?>, Field> emailFieldCache = new ConcurrentHashMap<>();

    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;

    public ValidationUtils(
            CustomerRepository customerRepository,
            ContactRepository contactRepository
            ) {
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
    }

    /**
     * Verifies that a customer exists with the given ID.
     * Throws CustomerNotFoundException if the customer is not found.
     *
     * @param customerId The ID of the customer to verify
     * @param callingClass The class name of the caller for logging purposes
     * @throws CustomerNotFoundException if the customer doesn't exist
     */
    public void verifyCustomerExists(Long customerId, String callingClass) {
        if (!customerRepository.existsById(customerId)) {
            logger.error("[{}] Customer not found with ID: {}", callingClass, customerId);
            throw new CustomerNotFoundException(customerId);
        }
        logger.debug("[{}] Verified customer exists with ID: {}", callingClass, customerId);
    }

    /**
     * Alternative version that automatically determines the calling class.
     * Slightly more convenient but may be less performant due to stack trace analysis.
     */
    public void verifyCustomerExists(Long customerId) {
        String callingClass = Thread.currentThread().getStackTrace()[2].getClassName();
        verifyCustomerExists(customerId, callingClass);
    }

    public void verifyCustomerHasContact(Long customerId) {
        if (!contactRepository.findByCustomerId(customerId).isEmpty()) {
            throw new CustomerHasAssociatedContactsException(customerId);
        }
    }

    /**
     * Verifies that an email is unique across all entities of a given type.
     * Used when creating new entities.
     *
     * @param email The email to verify
     * @param repository The repository to check against
     * @param entityName The name of the entity type (for logging)
     * @param <T> The entity type
     * @param <ID> The entity's ID type
     * @throws EmailAlreadyExistsException if the email already exists
     */
    public <T, ID> void verifyEmailNotExists(
            String email,
            JpaRepository<T, ID> repository,
            String entityName
    ) {
        if (email == null) {
            logger.error("[{}] Cannot verify null email", entityName);
            throw new IllegalArgumentException("Email cannot be null");
        }

        boolean exists = repository.findAll().stream()
                .map(this::getEmailFromEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(existingEmail -> existingEmail.equals(email));

        if (exists) {
            logger.error("[{}] Email already exists: {}", entityName, email);
            throw new EmailAlreadyExistsException(entityName);
        }
        logger.debug("[{}] Verified email is unique: {}", entityName, email);
    }

    /**
     * Verifies that an email update won't conflict with existing entities.
     * Used when updating existing entities.
     *
     * @param existingEntity The current entity
     * @param newEmail The proposed new email
     * @param repository The repository to check against
     * @param entityName The name of the entity type (for logging)
     * @param <T> The entity type
     * @param <ID> The entity's ID type
     * @throws EmailAlreadyExistsException if the new email conflicts
     */
    public <T, ID> void verifyEmailUpdate(
            T existingEntity,
            String newEmail,
            JpaRepository<T, ID> repository,
            String entityName
    ) {
        if (newEmail == null) {
            logger.error("[{}] Cannot update to null email", entityName);
            throw new IllegalArgumentException("New email cannot be null");
        }

        Optional<String> currentEmail = getEmailFromEntity(existingEntity);
        if (currentEmail.isEmpty()) {
            logger.error("[{}] Entity does not have an email field", entityName);
            throw new FieldNotFoundException(entityName, "email");
        }

        // If the email isn't changing, no need to check for conflicts
        if (newEmail.equals(currentEmail.get())) {
            logger.debug("[{}] Email unchanged, skipping conflict check", entityName);
            return;
        }

        // Check if any OTHER entity already uses this email
        boolean conflictExists = repository.findAll().stream()
                .map(this::getEmailFromEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(existingEmail ->
                        // Exclude the current entity's email and check for matches with the new email
                        !existingEmail.equals(currentEmail.get()) && existingEmail.equals(newEmail)
                );

        if (conflictExists) {
            logger.error("[{}] Cannot update email: {} already exists", entityName, newEmail);
            throw new EmailAlreadyExistsException(entityName);
        }

        logger.debug("[{}] Verified email update is valid: {}", entityName, newEmail);
    }

    /**
     * Helper method to safely extract email from any entity using reflection.
     * Uses a cache to improve performance by avoiding repeated reflection lookups.
     *
     * @param entity The entity to extract email from
     * @param <T> The entity type
     * @return Optional containing the email if found
     */
    private <T> Optional<String> getEmailFromEntity(T entity) {
        if (entity == null) {return Optional.empty();}

        Field emailField = emailFieldCache.computeIfAbsent(
                entity.getClass(),
                c -> ReflectionUtils.findField(c, "email"));

        if (emailField == null) {return Optional.empty();}

        ReflectionUtils.makeAccessible(emailField);
        Object value = ReflectionUtils.getField(emailField, entity);
        return Optional.ofNullable(value).map(Object::toString);
    }
}
