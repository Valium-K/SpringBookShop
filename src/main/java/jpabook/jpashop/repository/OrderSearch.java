package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    // where 문 param 이름
    private String memberName;
    private OrderStatus orderStatus;
}
