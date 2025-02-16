package io.github.cwyptt.crm.config;

import io.github.cwyptt.crm.entity.Contact;
import io.github.cwyptt.crm.entity.Customer;
import io.github.cwyptt.crm.repository.ContactRepository;
import io.github.cwyptt.crm.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
public class DataLoader {

    @Bean
    @Profile("dev")
    CommandLineRunner initDatabase(
            CustomerRepository customerRepository,
            ContactRepository contactRepository
    ) {
        return args -> {
            // Only load data if the repository is empty
            if(customerRepository.count() == 0) {
                // Create customers
                Customer customer1 = new Customer();
                customer1.setFirstName("John");
                customer1.setLastName("Doe");
                customer1.setEmail("john.doe@acmecorp.com");
                customer1.setPhone("555-123-4567");
                customer1.setCompany("Acme Corp");

                Customer customer2 = new Customer();
                customer2.setFirstName("Jane");
                customer2.setLastName("Smith");
                customer2.setEmail("jane.smith@example.com");
                customer2.setPhone("555-987-6543");
                customer2.setCompany("TechStart Inc");

                Customer customer3 = new Customer();
                customer3.setFirstName("Robert");
                customer3.setLastName("Johnson");
                customer3.setEmail("robert.johnson@example.com");
                customer3.setPhone("555-456-7890");
                customer3.setCompany("InnovateSolutions");

                Customer customer4 = new Customer();
                customer4.setFirstName("Maria");
                customer4.setLastName("Garcia");
                customer4.setEmail("maria.garcia@example.com");
                customer4.setPhone("555-789-0123");
                customer4.setCompany("Digital Dynamics");

                Customer customer5 = new Customer();
                customer5.setFirstName("David");
                customer5.setLastName("Lee");
                customer5.setEmail("david.lee@example.com");
                customer5.setPhone("555-321-6547");
                customer5.setCompany("Acme Corp");

                customerRepository.saveAll(Arrays.asList(
                        customer1, customer2, customer3, customer4, customer5
                ));

                System.out.println("Sample customers have been loaded successfully!");

                // Create contacts
                Contact contact1 = new Contact();
                contact1.setFirstName("Sarah");
                contact1.setLastName("Wilson");
                contact1.setEmail("sarah.wilson@acmecorp.com");
                contact1.setPhone("555-111-2222");
                contact1.setPosition("HR Director");
                contact1.setDepartment("Human Resources");
                contact1.setCustomer(customer1);
                contact1.setPrimary(true);

                Contact contact2 = new Contact();
                contact2.setFirstName("Michael");
                contact2.setLastName("Brown");
                contact2.setEmail("michael.brown@acmecorp.com");
                contact2.setPhone("555-333-4444");
                contact2.setPosition("IT Manager");
                contact2.setDepartment("Information Technology");
                contact2.setCustomer(customer1);
                contact2.setPrimary(false);

                // Contacts for TechStart Inc (customer2)
                Contact contact3 = new Contact();
                contact3.setFirstName("Emily");
                contact3.setLastName("Davis");
                contact3.setEmail("emily.davis@techstart.com");
                contact3.setPhone("555-555-6666");
                contact3.setPosition("CTO");
                contact3.setDepartment("Technology");
                contact3.setCustomer(customer2);
                contact3.setPrimary(true);

                // Contacts for InnovateSolutions (customer3)
                Contact contact4 = new Contact();
                contact4.setFirstName("James");
                contact4.setLastName("Taylor");
                contact4.setEmail("james.taylor@innovatesolutions.com");
                contact4.setPhone("555-777-8888");
                contact4.setPosition("Project Manager");
                contact4.setDepartment("Operations");
                contact4.setCustomer(customer3);
                contact4.setPrimary(true);

                Contact contact5 = new Contact();
                contact5.setFirstName("Lisa");
                contact5.setLastName("Anderson");
                contact5.setEmail("lisa.anderson@innovatesolutions.com");
                contact5.setPhone("555-999-0000");
                contact5.setPosition("Sales Director");
                contact5.setDepartment("Sales");
                contact5.setCustomer(customer3);
                contact5.setPrimary(false);

                // Contacts for Digital Dynamics (customer4)
                Contact contact6 = new Contact();
                contact6.setFirstName("Daniel");
                contact6.setLastName("Martin");
                contact6.setEmail("daniel.martin@digitaldynamics.com");
                contact6.setPhone("555-123-3211");
                contact6.setPosition("Marketing Manager");
                contact6.setDepartment("Marketing");
                contact6.setCustomer(customer4);
                contact6.setPrimary(true);

                // Contacts for Acme Corp (customer5)
                Contact contact7 = new Contact();
                contact7.setFirstName("Jennifer");
                contact7.setLastName("White");
                contact7.setEmail("jennifer.white@acmecorp.com");
                contact7.setPhone("555-456-7891");
                contact7.setPosition("Finance Director");
                contact7.setDepartment("Finance");
                contact7.setCustomer(customer5);
                contact7.setPrimary(true);

                contactRepository.saveAll(Arrays.asList(
                        contact1, contact2, contact3, contact4,
                        contact5, contact6, contact7
                ));

                System.out.println("Sample contacts have been loaded successfully!");
            }
        };
    }
}
