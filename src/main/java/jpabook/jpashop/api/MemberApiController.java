package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// @Controller @ResponseBody 이 둘을 합친것이 @RestController
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // ver 1.0
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) { // V1은 엔티티를 직접 받는 문제가 있다.
        Long id = memberService.join(member);
        System.out.println(id);
        return new CreateMemberResponse(id);
    }

    // ver 2.0
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2( //
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName()); // update에서 member를 return하지 않고(쿼리와 커맨드를 혼용하지 않고)
        Member foundMember = memberService.findOne(id); // 쿼리와 커맨드를 분리
        return new UpdateMemberResponse(foundMember.getId(), foundMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    @Data
    static class UpdateMemberRequest {
        private String name;

        public UpdateMemberRequest() {}
        public UpdateMemberRequest(String name) {
            this.name = name;
        }
    }
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }
}
