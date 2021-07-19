package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// @Controller @ResponseBody 이 둘을 합친것이 @RestController
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    // V1: 엔티티를 직접 뿌림 - 보안, 확장-관리문제를 가짐
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // V2
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> foundMembers = memberService.findMembers()  ;

        List<MemberDTO> collect = foundMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());

        // List를 그냥 내보내면 json배열 타입으로 되기에 한 번 감싼다.
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result <T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }

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
