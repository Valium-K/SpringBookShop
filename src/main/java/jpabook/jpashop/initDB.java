package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 *
 * userA
 * ㄴ jpa1 book
 * ㄴ jpa2 book
 *
 * userB
 * ㄴ spring1 book
 * ㄴ spring2 book
 */
@Component
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager entityManager;

        public void dbInit1() {

            // user 생성
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("test", "1", "111"));

            entityManager.persist(member);

            // book1생성
            Book book1 = new Book();
            book1.setName("jpa1 book");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            entityManager.persist(book1);

            // book2 생성
            Book book2 = new Book();
            book2.setName("jpa2 book");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            entityManager.persist(book2);

            // orderItem1, 2 생성
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            // 배송지 생성
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            // 주문 생성 - 한주문에 2개의 상품을 주문함
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            entityManager.persist(order);
        }
        public void dbInit2() {

            // user 생성
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("test2", "12", "112"));

            entityManager.persist(member);

            // book1생성
            Book book1 = new Book();
            book1.setName("spring1 book");
            book1.setPrice(30000);
            book1.setStockQuantity(100);
            entityManager.persist(book1);

            // book2 생성
            Book book2 = new Book();
            book2.setName("spring2 book");
            book2.setPrice(40000);
            book2.setStockQuantity(100);
            entityManager.persist(book2);

            // orderItem1, 2 생성
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 30000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            // 배송지 생성
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            // 주문 생성 - 한주문에 2개의 상품을 주문함
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            entityManager.persist(order);
        }
    }
}


