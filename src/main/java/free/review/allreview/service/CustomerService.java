package free.review.allreview.service;

import free.review.allreview.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface CustomerService {
    ResponseEntity<Page<Customer>> getAllResponse(Pageable pageRequest);

    ResponseEntity<Customer> getOneResponse(Long id);

    ResponseEntity<Customer> createNew(Customer contact, HttpServletRequest request);

    ResponseEntity<Customer> patchUpdate(Long id, Customer contactUpdates);

    ResponseEntity<Customer> delete(Long id);
}
