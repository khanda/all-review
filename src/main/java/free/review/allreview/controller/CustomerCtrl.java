package free.review.allreview.controller;

import free.review.allreview.entity.Customer;
import free.review.allreview.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class CustomerCtrl {
    @Autowired
    private CustomerService customerService;

    // List All Contacts
    @RequestMapping(value = "customers", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> getAllContacts() throws Throwable {
        return customerService.getAllContactsResponse();
    }

    // List One Customer
    @RequestMapping(value = "customers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getSingleContact(@PathVariable Long id) throws Throwable {
        return customerService.getSingleContactResponse(id);
    }

    // Create New Customer
    @RequestMapping(value = "customers", method = RequestMethod.POST)
    public ResponseEntity<Customer> createNewContact(@RequestBody Customer contact, HttpServletRequest req) {
        return customerService.createNewContact(contact, req);
    }

    // Update Customer with PATCH
    @RequestMapping(value = "customers/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Customer> patchUpdateContact(@PathVariable Long id, @RequestBody Customer contact) {
        return customerService.patchUpdateContact(id, contact);
    }

    // Update Customer with PUT
    @RequestMapping(value = "customers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> putUpdateContact(@PathVariable Long id, @RequestBody Customer contact) {
        return customerService.putUpdateContact(id, contact);
    }

    // Delete Customer
    @RequestMapping(value = "customers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteContact(@PathVariable Long id) {
        return customerService.deleteContact(id);
    }
}
