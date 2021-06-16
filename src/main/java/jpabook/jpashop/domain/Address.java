package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
