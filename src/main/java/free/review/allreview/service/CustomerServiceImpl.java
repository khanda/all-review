package free.review.allreview.service;

import free.review.allreview.BusinessConfig.CustomerConfig;
import free.review.allreview.entity.Customer;
import free.review.allreview.exceptions.MissingCustomerInfoException;
import free.review.allreview.exceptions.NotAllowChangeException;
import free.review.allreview.repository.CustomerRepository;
import free.review.allreview.specification.CustomerSpecificationsBuilder;
import free.review.allreview.utils.MyBeanUtil;
import free.review.allreview.utils.MyRepositoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public ResponseEntity<Page<Customer>> getAllResponse(Pageable pageRequest, Specification<Customer> spec) {
        Page<Customer> allContacts = customerRepository.findAll(spec, pageRequest);

        return new ResponseEntity<>(allContacts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> getOneResponse(Long id) {
        Customer getContact = findIfExists(id);
        return new ResponseEntity<>(getContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> createNew(Customer customer, HttpServletRequest request) {
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
    public ResponseEntity<Customer> patchUpdate(Long id, Customer customer) {
        List<String> notAllowChange = MyBeanUtil.filterNotAllowChange(customer, CustomerConfig.NOT_ALLOW_CHANGE);
        if (null != notAllowChange && !notAllowChange.isEmpty()) {
            throw new NotAllowChangeException();
        }
        Customer existingCustomer = findIfExists(id);

        MyBeanUtil.copyNonNullProperties(customer, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> delete(Long id) {

        Customer existingContact = findIfExists(id);
        customerRepository.delete(existingContact);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Customer findIfExists(Long id) {
        return MyRepositoryUtil.findIfExists(id, customerRepository);
    }

    private boolean isMissingInfo(Customer customer) {
        return null == customer ||
                ((null == customer.getName() || customer.getName().length() > 0) &&
                        null == customer.getPhone());

    }
}
