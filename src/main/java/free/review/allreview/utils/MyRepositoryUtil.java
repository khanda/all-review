package free.review.allreview.utils;

import free.review.allreview.exceptions.CustomerNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class MyRepositoryUtil {

    public static <T> T findIfExists(Long id, CrudRepository<T, Long> repository) {
        Optional<T> existingContact = repository.findById(id);

        if (existingContact.isPresent()) {
            return existingContact.get();
        } else {
            throw new CustomerNotFoundException();
        }
    }
}
