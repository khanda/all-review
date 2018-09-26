package free.review.allreview.controller;

import free.review.allreview.ParamHandler.ParamHandler;
import free.review.allreview.criteria.SearchCriteria;
import free.review.allreview.entity.Customer;
import free.review.allreview.service.CustomerService;
import free.review.allreview.specification.CustomerSpecification;
import free.review.allreview.specification.CustomerSpecificationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/")
public class CustomerCtrl {
    private final CustomerService customerService;

    @Autowired
    public CustomerCtrl(CustomerService customerService) {
        this.customerService = customerService;
    }

    // List All Contacts
    @RequestMapping(value = "customers", method = RequestMethod.GET)
    public ResponseEntity<Page<Customer>> getAllContacts(@RequestParam Integer page,
                                                         @RequestParam Integer limit,
                                                         @RequestParam String search) {
        CustomerSpecificationsBuilder builder = new CustomerSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Customer> spec = builder.build();

        Pageable pageable = ParamHandler.createPageable(page, limit, new Sort(Sort.Direction.ASC, "name"));
        return customerService.getAllResponse(pageable, spec);
    }

    // List One Customer
    @RequestMapping(value = "customers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getSingleContact(@PathVariable Long id) {
        return customerService.getOneResponse(id);
    }

    // Create New Customer
    @RequestMapping(value = "customers", method = RequestMethod.POST)
    public ResponseEntity<Customer> createNewContact(@RequestBody Customer contact, HttpServletRequest req) {
        return customerService.createNew(contact, req);
    }

    // Update Customer with PATCH
    @RequestMapping(value = "customers/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Customer> patchUpdateContact(@PathVariable Long id, @RequestBody Customer contact) {
        return customerService.patchUpdate(id, contact);
    }

//    // Update Customer with PUT
//    @RequestMapping(value = "customers/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Customer> putUpdate(@PathVariable Long id, @RequestBody Customer contact) {
//        return customerService.putUpdate(id, contact);
//    }

    // Delete Customer
    @RequestMapping(value = "customers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteContact(@PathVariable Long id) {
        return customerService.delete(id);
    }
}
