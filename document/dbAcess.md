# DB 접근기술

큰 그림에서
```
순수 JDBC
-> JDBC 템플릿
-> MyBatis
-> JPA
-> Spring Data JPA
```

## 1. 순수 JDBC

```Java
public Member findById(Long id) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = dataSource.getConnection();               // 연결
        pstmt = conn.prepareStatement("select * from member where id = ?");
        pstmt.setLong(1, id);
        rs = pstmt.executeQuery();                       // 실행
        if (rs.next()) {
            Member m = new Member();                     // 결과를 객체로 직접 변환
            m.setId(rs.getLong("id"));
            m.setName(rs.getString("name"));
            return m;
        }
        return null;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        close(conn, pstmt, rs);                          // 자원 해제도 직접
    }
}
```
- SQL한 줄을 위해, DB연결/해제/예외처리가 모두 순수 코드 작성으로 이뤄져야함.
- close를 빼먹으면 커넥션이 새는 버그 발생

## 2. JdbcTemplate 
```java
public Member findById(Long id) {
    return jdbcTemplate.queryForObject(
        "select * from member where id = ?",
        (rs, rowNum) -> new Member(rs.getLong("id"), rs.getString("name")),
        id
    );
}
```

- 연결, 해제, 예외 변환을 스프링이 내부에서 알아서 처리
- SQL은 여전히 직접 작성해야함.

## 3. JPA
```java
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String name;
}

public Member findById(Long id) {
    return em.find(Member.class, id);   // SQL을 JPA가 자동 생성
}

public void save(Member member) {
    em.persist(member);                 // insert문도 자동
}
```

- 객체와 DB테이블을 연결해 두면 SQL을 JPA가 대신 만들어 줌.
- JPA는 표준 인터페이스이고, 실제로 구현한 것이 `Hibernate`
- 내부 구현 상세를 모르고 작성하면 성능 문제가 발생하기 쉬움.

## 4. Spring Data JPA
```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);   // 메서드 이름으로 쿼리 자동 생성
}
```

- save, findById, findAll 처럼 기본기능이 추상화 되어 있음.
- 매서드 이름 규칙만 이해하면 더 쉬운 문법 구사 가능