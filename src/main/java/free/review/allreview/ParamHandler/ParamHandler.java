package free.review.allreview.ParamHandler;

import free.review.allreview.constant.PagingConstant;
import free.review.allreview.exceptions.InvalidParamsException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ParamHandler {
    public static boolean isPagingParamValid(Integer page, Integer limit) {
        return null != page && page > 0;
    }

    public static Pageable createPageable(Integer page, Integer limit, Sort sort) {
        if (null == page || page <= 0) throw new InvalidParamsException();

        if (page > 0) {
            page--;//page start at 0
        }
        if (null == limit || limit <= 0) {
            limit = PagingConstant.LIMIT;
        }

        return new PageRequest(page, limit, sort);
    }
}
