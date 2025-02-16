package io.github.cwyptt.crm.utility.constant;

public class QueryConstants {
    public static final String SEARCH_CUSTOMERS_QUERY =
            "SELECT c FROM Customer c WHERE " +
                    "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
                    "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
                    "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
                    "(:phone IS NULL OR c.phone LIKE CONCAT('%', :phone, '%')) AND " +
                    "(:company IS NULL OR LOWER(c.company) LIKE LOWER(CONCAT('%', :company, '%')))";

    public static final String SEARCH_CONTACTS_QUERY =
            "SELECT c FROM Contact c WHERE " +
                    "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
                    "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
                    "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
                    "(:phone IS NULL OR c.phone LIKE CONCAT('%', :phone, '%')) AND " +
//                    "(:position IS NULL OR LOWER(c.position) LIKE LOWER(CONCAT('%', :position, '%'))) AND " +
//                    "(:department IS NULL OR LOWER(c.department) LIKE LOWER(CONCAT('%', :department, '%'))) AND " +
                    "(:customerId IS NULL OR c.customer.id = :customerId)";

    public static final String PRIMARY_CONTACT_QUERY =
            "SELECT c FROM Contact c WHERE c.customer.id = :customerId AND c.isPrimary = true";
}
