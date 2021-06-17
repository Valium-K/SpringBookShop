도메인 모델과 테이블 설계
=====================

### @OneToOne
* FK를 아무곳이나 두어도 되지만, 다른 FK가 있는 테이블이 있다면 같이 두는 것이 좋다.

### @ManyToMany
* 1:N - N:1로 각각의 Id만 있는 중간테이블이 자동생성 된다. 
    문제는 속성을 직접 추가 할 수 없어서 **쓰지 않는다.**
  
### @Getter, @Setter
* @Getter는 읽기라 상관 없지만, @Setter는 쓰기라 필요할 때 만 제공해야한다.    
    Setter대신 병경 지점이 명확하도록 변경을 위한 비지니스 메서드를 별도로 제공하는 편이 좋다.
  
### @Colum(name = "Entity_id")
* 객체는 Entity.id로 id 필드만으로 명확하지만, 테이블에서는 명확히 파악하기 어렵기에 관례상 사용한다.

### @값 타입
* Immutable 하게 만들어야한다. 즉, setter를 빼고, 기본생성자는 private or protected로 막은 후 생성자 오버로딩을 통해 set 해야한다.

### 모든 연관관계는 지연로딩으로 설정해야한다.
* XToOne(ManyToOne, OneToOne)은 기본이 EAGER 이므로 LAZY로 바꿔야한다.
  * 예로 엔티티 A와 연관된 엔티티 하나가 B이고 그것이 EAGER 면 큰 문제가 없는것 같지만,    
    JPQL에서 엔티티A를 모두 가져오는 쿼리를 날리면 N + 1문제가 발생한다.
* 만약 연관된 엔티티를 함께 DB에서 조회해야 한다면, 우선순위가 더 높은 fetch join을 사용하면 EAGER와 같이 동작한다.

### 컬렉션은 생성자 초기화보다 필드 초기화가(조금 더) 안전하다

### 테이블, 컬럼명 전략
* Hibernate 버전, Spring framework 버전별로 다르다.
    * Hibernate: 엔티티의 필드명을 그대로 사용
    * Spring framework: 카멜 케이스, 점 -> 언더스코어, 대문자 -> 소문자
        * `SpringPhysicalNamingStrategy`를 변경가능하다.
    
### 양방향 연관관계
* 연관관계 편의 메서드를 만드는 것이 좋다.
