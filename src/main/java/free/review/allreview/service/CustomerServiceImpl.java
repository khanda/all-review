package free.review.allreview.service;

import free.review.allreview.entity.Customer;
import free.review.allreview.exceptions.CustomerNotFoundException;
import free.review.allreview.exceptions.MissingCustomerInfoException;
import free.review.allreview.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        Assert.notNull(customerRepository, "customerRepository must not be null!");
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<List<Customer>> getAllContactsResponse() {
        List<Customer> allContacts = customerRepository.findAll();

        return new ResponseEntity<>(allContacts, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Customer> getSingleContactResponse(Long id) {
        Customer getContact = findContactIfExists(id);
        return new ResponseEntity<>(getContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> createNewContact(Customer customer, HttpServletRequest request) {
        if (null != customer.getName() && customer.getName().length() > 0) {
            Customer newContact = customerRepository.saveAndFlush(customer);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Location", contactUrlHelper(newContact, request));

            return new ResponseEntity<>(newContact, responseHeaders, HttpStatus.CREATED);
        } else {
            throw new MissingCustomerInfoException();
        }
    }

    @Override
    public ResponseEntity<Customer> patchUpdateContact(Long id, Customer customer) {
        Customer existingContact = findContactIfExists(id);
        if (isMissingInfo(customer)) {
            throw new MissingCustomerInfoException();
        }

        if (null != customer.getName()) {
            existingContact.setName(customer.getName());
        }
        if (null != customer.getPhone()) {
            existingContact.setPhone(customer.getPhone());
        }


        Customer updatedContact = customerRepository.saveAndFlush(existingContact);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Customer> putUpdateContact(Long id, Customer customer) {
        Customer existingContact = findContactIfExists(id);

        if (isMissingInfo(customer)) {
            throw new MissingCustomerInfoException();
        }

        BeanUtils.copyProperties(customer, existingContact);
        Customer updatedContact = customerRepository.saveAndFlush(existingContact);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> deleteContact(Long id) {

        Customer existingContact = findContactIfExists(id);
        customerRepository.delete(existingContact);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private String contactUrlHelper(Customer customer, HttpServletRequest request) {
        StringBuilder resourcePath = new StringBuilder();

        resourcePath.append(request.getRequestURL());
        resourcePath.append("/");
        resourcePath.append(customer.getId());

        return resourcePath.toString();
    }

    private Customer findContactIfExists(Long id) {
        Customer existingContact = customerRepository.getOne(id);

        if (null != existingContact.getId()) {
            return existingContact;
        } else {
            throw new CustomerNotFoundException();
        }
    }

    private boolean isMissingInfo(Customer customer) {
        return null == customer ||
                ((null == customer.getName() || customer.getName().length() > 0) &&
                        null == customer.getPhone());

    }
}
