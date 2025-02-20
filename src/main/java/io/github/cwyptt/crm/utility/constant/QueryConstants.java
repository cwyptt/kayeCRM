package io.github.cwyptt.crm.utility.constant;

public class QueryConstants {
    public static final String SEARCH_CUSTOMERS_QUERY =
            "SELECT c FROM Customer c WHERE " +
                    "(:status IS NULL OR c.status = :status) AND " +
                    "(:companyId IS NULL OR c.company.id = :companyId) AND " +
                    "(:contactId IS NULL OR c.contact.id = :contactId) AND " +
                    "(:since IS NULL OR c.customerSince >= :since)";

    public static final String SEARCH_CONTACTS_QUERY =
            "SELECT c FROM Contact c WHERE " +
                    "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
                    "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
                    "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
                    "(:phone IS NULL OR c.phone.number LIKE %:phone%) AND " +
                    "(:companyId IS NULL OR c.company.id = :companyId) AND " +
                    "(:isCustomer IS NULL OR c.isCustomer = :isCustomer)";
//                    "(:position IS NULL OR LOWER(c.position) LIKE LOWER(CONCAT('%', :position, '%'))) AND " +
//                    "(:department IS NULL OR LOWER(c.department) LIKE LOWER(CONCAT('%', :department, '%'))) AND " +

    public static final String PRIMARY_CONTACT_QUERY =
            "SELECT c FROM Contact c WHERE c.customer.id = :customerId AND c.isPrimary = true";

    public static final String SEARCH_COMPANIES_QUERY =
            "SELECT c FROM Company c WHERE " +
                    "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                    "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
                    "(:phone IS NULL OR c.phone.number LIKE CONCAT('%', :phone, '%')) AND " +
                    "(:industry IS NULL OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :industry, '%')))";
}
