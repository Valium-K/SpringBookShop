package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

  /** Spring Data JPA 를 사용 시 @Autowired를 대신 사용해도 된다. (표준은 @PersistenceContext)
   *  그러므로 생성자 1개 + @RequiredArgsConstructor 를 사용해 생략가능

    // spring이 EntityManager를 주입해준다.
    // 패키지는 javax.persistence.PersistenceContext 임을 염두
    @PersistenceContext
    private EntityManager em;

  */

  private final EntityManager em;

//    // 만약 EntityManagerFactory를 주입받고 싶다면 사용
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public void save(Member member) {
        // 영속성 컨텍스트에 추가 전 member에 id값을 자동으로 넣는다는 것을 염두하자.
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        String qlString = "select m from Member m";
        return em.createQuery(qlString, Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        String qlString = "select m from Member m where m.name = :targetName";

        return em.createQuery(qlString, Member.class)
                .setParameter("targetName", name)
                .getResultList();
    }
}
