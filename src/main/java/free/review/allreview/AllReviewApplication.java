package free.review.allreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AllReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllReviewApplication.class, args);
    }
}
