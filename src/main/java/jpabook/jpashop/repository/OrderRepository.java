package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order>findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql ="select o From Order o join o.member m";boolean isFirstCondition =true;

        //주문상태검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원이름검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }

            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        //최대 1000건
        if(orderSearch.getOrderStatus()!=null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        } if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        String qlString = "select o from Order as o" +
                            " join fetch o.member as m" +
                            " join fetch o.delivery as d";

        return em.createQuery(qlString, Order.class).getResultList();
    }

    public List<Order> findAllWithItem() {

        // order에 대한 조인의 결과가 뻥튀기 되기에 distinct를 해준다.
        // jpql에서 distinct는 쿼리문에도 들어간다. 하지만 이 경우는 DB에서 제거되는 행이 없기에
        // 뻥튀기 된 결과에 jpql이 추가적으로 distinct를 한다.
        // 쿼리는 1줄 나간다.
        return em.createQuery("select distinct o from Order as o" +
                                    " join fetch o.member as m" +
                                    " join fetch o.delivery as d" +
                                    " join fetch o.orderItems as oi" +
                                    " join fetch oi.item as i", Order.class).getResultList();
    }
}
