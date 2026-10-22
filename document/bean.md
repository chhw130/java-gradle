# Spring 빈

## 1. 요약
스프링 빈은 스프링이 대신 만들어 관리해 주는 객체.

## 2. 필요한 이유
````java
public class OrderService{
    private MemberRepository repo = new MemoryMemberRepository();
}
````

이렇게 작성하면 생기는 문제점.
- 강한 결합 : 저장소를 JdbcMemberRepository로 바꾸려면 OrderService코드를 직접 고쳐야함.
- 객체 관리 부담 : 어떤 객체를 언제 몇 개 만들지 개발자가 챙겨야함.

즉, 이런 부담을 스프링이 연결해줌. IOC

## 3. 구성요소

- 스프링 컨테이너 : 빈을 만들고 보관하고 관리하는 곳
- 빈 : 컨테이너가 관리하는 객체
- 의존성 주입 DI : 필요한 빈을 찾아서 넣어 주는 일

## 4. 동작원리

1. 앱 실행 (main -> SpringAppliction)
2. 스프링 컨테이너 생성
3. 컴포넌트 스캔 : @Component 붙은 클래스 찾기
4. 찾은 클래스로 객체 생성 -> 컨테이너에 등록(=빈)
5. 의존관계 주입
6. 앱 사용 준비 완료

SpringBootAppliction안에는 메인 클래스가 있는 패키지와 하위 패키지를 뒤져 어노테이션을 찾음.

## 5. 예제
```java
@Repository
public class MemberRepository{}

@Service
public class MemberService{
    private final MemberRepository repo;
    
    // 생성자가 하나면 AutoWired 생략 가능
    public MemberService(MemberRepository repo){
        this.repo = repo;
    }
}
```

MemberService는 `new MemberRepositry()`를 직접 생성하지 않음.

## 6. 빈을 등록하는 두가지 방법

1. 자동 등록 : 위의 예시
2. 수동 등록 : @Configuration과 @Bean사용
```java
@Configuration
public class AppConfig{
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCyrptPasswordEncoder();
    }
}
```
외부 라이브러리 클래스처럼 내가 @Component를 붙일 수 없을 때 주로 사용.

## 7. 정리

1. 빈은 스프링 컨테이너가 관리하는 객체
2. 스프링 부트는 실행될 때 컴포넌트 스캔으로 @Component계열 클래스를 찾아 빈 등록
3. 필요한 빈은 DI로 자동으로 연결
4. 기본은 싱글톤이라 상태를 가진 필드는 피해야함.
5. 