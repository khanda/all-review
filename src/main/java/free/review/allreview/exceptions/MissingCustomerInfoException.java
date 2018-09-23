package free.review.allreview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Missing Customer Info")
public class MissingCustomerInfoException extends RuntimeException {
}
