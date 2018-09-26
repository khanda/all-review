package free.review.allreview.service;

import free.review.allreview.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CustomerService {
    ResponseEntity<Page<Customer>> getAllResponse(Pageable pageRequest, Specification<Customer> spec);

    ResponseEntity<Customer> getOneResponse(Long id);

    ResponseEntity<Customer> createNew(Customer contact, HttpServletRequest request);

    ResponseEntity<Customer> patchUpdate(Long id, Customer contactUpdates);

    ResponseEntity<Customer> delete(Long id);
}
