package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@Transactional
@DisplayName("orderService 통합 테스트")
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("jpa", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order gotOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 order", OrderStatus.ORDER, gotOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야한다.", 1, gotOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, gotOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
    }



    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("jpa", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order gotOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 cancel 이다.", OrderStatus.CANCEL, gotOrder.getStatus());
        assertEquals("주문 취소 후 재고가 다시 증가하낟.", 10, book.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량_초과() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("jpa", 10000, 10);

        int orderCount = 11;
        // when

        // then
        Assertions.assertEquals("need more stock.", assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        }).getMessage());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);

        em.persist(book);
        return book;
    }
    private Member createMember() {
        Member member = new Member();
        member.setName("m1");
        member.setAddress(new Address("서울", "길", "123-123"));

        em.persist(member);
        return member;
    }
}