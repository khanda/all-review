package free.review.allreview.BusinessConfig;

import java.util.Arrays;
import java.util.List;

public class GoodsPriceConfig {

    public static final List<String> NOT_ALLOW_CHANGE = Arrays.asList(
            "id",
            "date",
            "goodsId"
    );
}
