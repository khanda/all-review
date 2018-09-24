package free.review.allreview.service;

import free.review.allreview.BusinessConfig.CustomerConfig;
import free.review.allreview.entity.Customer;
import free.review.allreview.exceptions.CustomerNotFoundException;
import free.review.allreview.exceptions.MissingCustomerInfoException;
import free.review.allreview.exceptions.NotAllowChangeException;
import free.review.allreview.repository.CustomerRepository;
import free.review.allreview.utils.MyBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        Assert.notNull(customerRepository, "customerRepository must not be null!");
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<Iterable<Customer>> getAllContactsResponse() {
        Iterable<Customer> allContacts = customerRepository.findAll();

        return new ResponseEntity<>(allContacts, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Customer> getSingleContactResponse(Long id) {
        Customer getContact = findContactIfExists(id);
        return new ResponseEntity<>(getContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> createNewContact(Customer customer, HttpServletRequest request) {
        try {
            Customer newCustomer = customerRepository.save(customer);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Location", newCustomer.getId().toString());

            return new ResponseEntity<>(newCustomer, responseHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new MissingCustomerInfoException();
        }
    }

    @Override
    public ResponseEntity<Customer> patchUpdateContact(Long id, Customer customer) {
        List<String> notAllowChange = filterNotAllowChange(customer);
        if (null != notAllowChange && !notAllowChange.isEmpty()) {
            throw new NotAllowChangeException();
        }
        Customer existingCustomer = findContactIfExists(id);

        MyBeanUtil.copyNonNullProperties(customer, existingCustomer);
        Customer updatedContact = customerRepository.save(existingCustomer);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);


    }

    private List<String> filterNotAllowChange(Customer customer) {
        Field[] fields = Customer.class.getDeclaredFields();

        List<String> notAllowChangeFields = new ArrayList<>();
        for (Field field : fields) {
            Object o = MyBeanUtil.getProperty(customer, field.getName());
            if (null != o && CustomerConfig.NOT_ALLOW_CHANGE.indexOf(field.getName()) >= 0) {
                notAllowChangeFields.add(field.getName());
            }
        }

        return notAllowChangeFields;
    }

    @Override
    public ResponseEntity<Customer> putUpdateContact(Long id, Customer customer) {
        Customer existingContact = findContactIfExists(id);

        if (isMissingInfo(customer)) {
            throw new MissingCustomerInfoException();
        }

        BeanUtils.copyProperties(customer, existingContact);
        existingContact.setId(id);
        Customer updatedContact = customerRepository.save(existingContact);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> deleteContact(Long id) {

        Customer existingContact = findContactIfExists(id);
        customerRepository.delete(existingContact);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Customer findContactIfExists(Long id) {
        Optional<Customer> existingContact = customerRepository.findById(id);

        if (existingContact.isPresent()) {
            return existingContact.get();
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
