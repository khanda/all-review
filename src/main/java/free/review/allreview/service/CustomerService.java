package free.review.allreview.service;

import free.review.allreview.entity.Customer;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CustomerService {
    ResponseEntity<List<Customer>> getAllContactsResponse();

    ResponseEntity<Customer> getSingleContactResponse(Long id);

    ResponseEntity<Customer> createNewContact(Customer contact, HttpServletRequest request);

    ResponseEntity<Customer> patchUpdateContact(Long id, Customer contactUpdates);

    ResponseEntity<Customer> putUpdateContact(Long id, Customer contactUpdates);

    ResponseEntity<Customer> deleteContact(Long id);
}
