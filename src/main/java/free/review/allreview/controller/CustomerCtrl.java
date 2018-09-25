package free.review.allreview.controller;

import free.review.allreview.ParamHandler.ParamHandler;
import free.review.allreview.entity.Customer;
import free.review.allreview.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<Page<Customer>> getAllContacts(@RequestParam Integer page, @RequestParam Integer limit) throws Throwable {
        Pageable pageable = ParamHandler.createPageable(page, limit, new Sort(Sort.Direction.ASC, "name"));
//        new Sort(Sort.Direction.DESC, "description").and(new Sort(Sort.Direction.ASC, "title")
        return customerService.getAllResponse(pageable);
    }

    // List One Customer
    @RequestMapping(value = "customers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getSingleContact(@PathVariable Long id) throws Throwable {
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
