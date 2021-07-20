package jpabook.jpashop.repository.order.simpleQuery;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
public class OrderSimpleQuery {
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        // jpa는 entity나 valueObject(embeddable type)만 return 가능하다.
        // 그 외에는 new 오퍼레이션을 붙여야 return 가능하다.
        // 이 때 new 오퍼레이션의 엔티티는 절대경로를 붙인 생성자에다 값타입만 파라미터로 넘겨서 객체를 new 할 수있다.
        return em.createQuery("select new jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join Member m" +
                " join Delivery d", OrderSimpleQueryDto.class).getResultList();
    }
}
