package io.github.cwyptt.crm.config;

import io.github.cwyptt.crm.entity.Company;
import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.enums.CustomerStatus;
import io.github.cwyptt.crm.repository.CompanyRepository;
import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import io.github.cwyptt.crm.utility.PhoneNumberConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    @Profile("dev")
    CommandLineRunner initDatabase(
            CompanyRepository companyRepository,
            ContactRepository contactRepository,
            CustomerRepository customerRepository,
            PhoneNumberConverter converter
    ) {
        return args -> {
            // Only load data if repositories are empty
            if (companyRepository.count() == 0) {
                // Create companies first
                Company acmeCorp = new Company();
                acmeCorp.setName("Acme Corp");
                acmeCorp.setEmail("info@acmecorp.com");
                acmeCorp.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                acmeCorp.setWebsite("https://www.acmecorp.com");
                acmeCorp.setIndustry("Technology");
                acmeCorp.setDescription("Leading provider of innovative solutions");

                Company techStart = new Company();
                techStart.setName("TechStart Inc");
                techStart.setEmail("info@techstart.com");
                techStart.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                techStart.setWebsite("https://www.techstart.com");
                techStart.setIndustry("Software");
                techStart.setDescription("Startup focused on cutting-edge technology");

                Company innovateSolutions = new Company();
                innovateSolutions.setName("InnovateSolutions");
                innovateSolutions.setEmail("info@innovatesolutions.com");
                innovateSolutions.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                innovateSolutions.setWebsite("https://www.innovatesolutions.com");
                innovateSolutions.setIndustry("Consulting");
                innovateSolutions.setDescription("Business innovation consulting");

                Company digitalDynamics = new Company();
                digitalDynamics.setName("Digital Dynamics");
                digitalDynamics.setEmail("info@digitaldynamics.com");
                digitalDynamics.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                digitalDynamics.setWebsite("https://www.digitaldynamics.com");
                digitalDynamics.setIndustry("Digital Marketing");
                digitalDynamics.setDescription("Digital transformation experts");

                List<Company> companies = companyRepository.saveAll(Arrays.asList(
                        acmeCorp, techStart, innovateSolutions, digitalDynamics
                ));

                System.out.println("Sample companies have been loaded successfully!");

                // Create contacts
                Contact contact1 = new Contact();
                contact1.setFirstName("Sarah");
                contact1.setLastName("Wilson");
                contact1.setEmail("sarah.wilson@acmecorp.com");
                contact1.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                contact1.setPosition("HR Director");
                contact1.setDepartment("Human Resources");
                contact1.setCompany(acmeCorp);
                contact1.setPrimaryContact(true);

                Contact contact2 = new Contact();
                contact2.setFirstName("Michael");
                contact2.setLastName("Brown");
                contact2.setEmail("michael.brown@acmecorp.com");
                contact2.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                contact2.setPosition("IT Manager");
                contact2.setDepartment("Information Technology");
                contact2.setCompany(acmeCorp);
                contact2.setPrimaryContact(false);

                Contact contact3 = new Contact();
                contact3.setFirstName("Emily");
                contact3.setLastName("Davis");
                contact3.setEmail("emily.davis@techstart.com");
                contact3.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                contact3.setPosition("CTO");
                contact3.setDepartment("Technology");
                contact3.setCompany(techStart);
                contact3.setPrimaryContact(true);

                Contact contact4 = new Contact();
                contact4.setFirstName("James");
                contact4.setLastName("Taylor");
                contact4.setEmail("james.taylor@innovatesolutions.com");
                contact4.setPhone(converter.convertToEntityAttribute("555-100-0000"));
                contact4.setPosition("Project Manager");
                contact4.setDepartment("Operations");
                contact4.setCompany(innovateSolutions);
                contact4.setPrimaryContact(true);

                List<Contact> contacts = contactRepository.saveAll(Arrays.asList(
                        contact1, contact2, contact3, contact4
                ));

                System.out.println("Sample contacts have been loaded successfully!");

                // Create customers
                Customer customer1 = new Customer();
                customer1.setContact(contact1);
                customer1.setCompany(acmeCorp);
                customer1.setStatus(CustomerStatus.ACTIVE);
                customer1.setCustomerSince(LocalDateTime.now().minusMonths(6));
                customer1.setAccountNumber("ACC-001");
                customer1.setPaymentTerms("Net 30");
                customer1.setBillingPreferences("Email");
                customer1.setNotes("Key account - Monthly review required");

                Customer customer2 = new Customer();
                customer2.setContact(contact3);
                customer2.setCompany(techStart);
                customer2.setStatus(CustomerStatus.ACTIVE);
                customer2.setCustomerSince(LocalDateTime.now().minusMonths(3));
                customer2.setAccountNumber("ACC-002");
                customer2.setPaymentTerms("Net 15");
                customer2.setBillingPreferences("Email");
                customer2.setNotes("Startup with high growth potential");

                Customer customer3 = new Customer();
                customer3.setContact(contact4);
                customer3.setCompany(innovateSolutions);
                customer3.setStatus(CustomerStatus.ACTIVE);
                customer3.setCustomerSince(LocalDateTime.now().minusMonths(1));
                customer3.setAccountNumber("ACC-003");
                customer3.setPaymentTerms("Net 30");
                customer3.setBillingPreferences("Mail");
                customer3.setNotes("New customer - Regular follow-up needed");

                List<Customer> customers = customerRepository.saveAll(Arrays.asList(
                        customer1, customer2, customer3
                ));

                // Update contacts to reflect customer status
                contact1.setCustomer(true);
                contact3.setCustomer(true);
                contact4.setCustomer(true);
                contactRepository.saveAll(Arrays.asList(contact1, contact3, contact4));

                System.out.println("Sample customers have been loaded successfully!");
            }
        };
    }
}