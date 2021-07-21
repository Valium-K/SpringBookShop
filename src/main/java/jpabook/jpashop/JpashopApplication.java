package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpashopApplication.class, args);
    }

    @Bean
    Hibernate5Module hibernate5Module() {
        // 지연로딩(프록시 이용) 에서 초기화가 된 것들만 api로 반환.
        return new Hibernate5Module();

        // 즉시로딩
        // return new Hibernate5Module().configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
    }
}
