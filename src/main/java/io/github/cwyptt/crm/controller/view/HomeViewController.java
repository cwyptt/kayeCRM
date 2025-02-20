package io.github.cwyptt.crm.controller.view;

import io.github.cwyptt.crm.enums.CustomerStatus;
import io.github.cwyptt.crm.service.ActivityService;
import io.github.cwyptt.crm.service.CompanyService;
import io.github.cwyptt.crm.service.ContactService;
import io.github.cwyptt.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequiredArgsConstructor
public class HomeViewController {

    private final CustomerService customerService;
    private final ContactService contactService;
    private final CompanyService companyService;
    private final ActivityService activityService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeTab", "home");
        model.addAttribute("pageTitle", "Home");

        // Calculate start of current month
        LocalDateTime startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());

        model.addAttribute("totalCustomers", customerService.getAllCustomers().size());
        model.addAttribute("activeCustomers", customerService.getCustomersByStatus(CustomerStatus.ACTIVE).size());
        model.addAttribute("totalContacts", contactService.getAllContacts().size());
        model.addAttribute("totalCompanies", companyService.getAllCompanies().size());
        model.addAttribute("newThisMonth", customerService.getCustomersCreatedSince(startOfMonth).size());

        // Recent activities
        model.addAttribute("activities", activityService.getRecentActivities());

        return "home/index";


    }
}
