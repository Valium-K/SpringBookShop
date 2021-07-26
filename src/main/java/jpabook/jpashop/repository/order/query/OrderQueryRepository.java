package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 화면 등을 위한 repo
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        // 1:1 부분만 채워받음 - 컬렉션은 빈 상태
        List<OrderQueryDto> result = findOrders(); // query 1번 -> N

        // 이제 컬렉션 부분에 대한 쿼리를 따로 날림
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // Query N번
            o.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
         return em.createQuery(
                 "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                         " from OrderItem oi" +
                         " join oi.item i" +
                         " where oi.order.id = :orderId", OrderItemQueryDto.class)
                 .setParameter("orderId", orderId)
                 .getResultList();
    }


    // 1:N 관계에서 1:1 부분을 먼저 풀어냄
    // 한번에 join해서 가져오면 row가 늘어나기 때문
    private List<OrderQueryDto> findOrders() {
        // queryDSL 사용 시 더 깔끔하게 사용 가능
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d",OrderQueryDto.class)
                .getResultList();
    }





    public List<OrderQueryDto> findAllByDtoOptimized() {
        List<OrderQueryDto> result = findOrders();

        // 아래 쿼리의 in절을 위해 orderid를 result에서 변환한다.
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        // 루프를 돌지 않고 한번에 가져온다
        List<OrderItemQueryDto> orderItems =  em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // Map으로 변환
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        // result에 세팅
        for (OrderQueryDto orderQueryDto : result) {
            orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId()));
        }

        return result;
    }

    public List<OrderFlatDto> findAllByDtoFlat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
