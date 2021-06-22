package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 회원가입을 위한 데이터 폼
 * Member를 직접 쓰기엔 어려운 상황이 대부분이기에 데이터 폼을 만듦.
 */
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "이름은 필수 입력 사항입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
