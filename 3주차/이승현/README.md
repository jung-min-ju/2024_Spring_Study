# 2024_Spring_Study

### 3주차 페이징

# 페이징이 뭐야?

페이징은 사용자가 데이터를 요청했을 때, 전체 데이터 중 일부를 원하는 정렬 방식으로 보여주는 방식이다.

## 페이징을 왜 쓸까?

### 1. 데이터 관리의 효율성

만약 페이징을 사용하지 않고 수많은 데이터를 한번에 클라이언트로 보내게 된다면 성능에 큰 문제가 생길 수 있을 것이다.

그래서 우리는 페이징을 사용해 클라이언트가 필요한 정보만 요청하고 전송함으로써 네트워크와 서버 자원을 절약할 수 있다.

### 2. 사용자 경험 향상

페이징을 사용하지 않고 수많은 데이터를 클라이언트에게 보내도 성능에 문제가 생기지 않는다고 생각해보자.

엄청난 양의 데이터를 클라이언트에게 바로 보낸다면 사용자가 필요한 정보를 찾기 더욱 어려워질 것이다.

그래서 우리는 페이징을 사용해서 사용자가 필요한 데이터를 더 쉽게 찾게할 수 있다.

이로 인해 사용자 경험이 향상될 것이다.

### 3. 성능 최적화

1번 내용과 유사하게 페이징으로 필요한 만큼만 데이터를 처리해서 서버 성능을 최적화할 수 있다.

# 페이징 어떻게 쓸까?

페이징을 사용하기 위해서는 크게 세가지 파라미터를 알아야한다.

- page : 페이징이 적용되었을 때 사용자가 원하는 페이지 번호
- size : 각각의 페이지에 담을 데이터의 개수
- sort : 페이지를 어떻게 정렬하여 제공할 것인지

위의 세가지 파라미터를 아래에서 설명할 Pageable 객체에 담아 페이징을 사용 할 수 있다.

### Pageable

Pageable은 페이징 처리를 위해 정보를 저장하는 인터페이스 객체이다.

pageable은 아래와 같은 메서드를 지원한다.

| getPageNumber() | 현재 페이지 번호 반환(0부터 시작) |
| --- | --- |
| getPageSize() | 한 페이지당 최대 항목 수 반환 |
| getOffset() | 현재 페이지의 시작 위치 반환 |
| getSort() | 정렬 정보 반환 |
| next() | 다음 페이지 정보 반환 |
| previous() | 이전 페이지 정보 반환 |

해당 Pageable 인터페이스는 PageRequest로 구현된다.

### PageRequest

Pageable의 구현체로, 페이지 정보를 생성하는 클래스이다.

페이지 번호, 페이지 당 항목 수, 정렬 정보를 이용하여 Pageable 인터페이스를 구현한다.

```java
public static PageRequest of(int pageNumber, int pageSize, Sort sort) {
        return new PageRequest(pageNumber, pageSize, sort);
    }
    
public static PageRequest of(int pageNumber, int pageSize) {
        return of(pageNumber, pageSize, Sort.unsorted());
    }
```

위와 같이 PageRequest는 of 메서드를 사용하여 인스턴스를 생성할 수 있다.

인자로 page, size를 주고, sort는 선택적으로 줄 수 있다.

페이지 처리는 위와 같이 Pageable, PageRequest를 이용하여 사용할 수 있다.

Spring Data JPA 에는 Pageable, PageRequest로 만든 페이징 처리를 위한 두 가지 객체가 존재한다.

바로 Slice와 Page 이다.

## Page, Slice

우리가 페이징을 사용할때엔 아래와 같이 사용한다.

```java
public interface PostRepository extends JpaRepository<Post, Integer> {
	Slice<Post> findSliceBy(Pageable pageable);
	Page<Post> findPageBy(Pageable pageable);
}
```

위의 코드를 보면 반환 값으로 Slice와 Page 두가지가 쓰인다.

이 둘의 차이는 뭘까?

### Slice

```java
public interface Slice<T> extends Streamable<T>{
	...
}
```

Slice는 Streamable을 상속받는 인터페이스이다. 여기서 streamable은 크게 중요하지 않아 보인다.

대충 설명하자면 컬렉션을 보다 유연하고 간결하게 처리할 수 있게 해준다고 한다.

Slice는 페이징과 관련된 여러 메서드를 가지고 있다.

| 반환 타입 | 메서드 | 설명 |
| --- | --- | --- |
| List<T> | getContent() | 페이지 내용을 리스트로 반환 |
| int | getNumber() | 현재 페이지 번호 반환 |
| int | getNumberOfElement() | 현재 페이지의 데이터 개수 반환 |
| int | getSize() | 슬라이스의 사이즈 반환 |
| boolean | hasContent() | 슬라이스에 데이터가 존재하는지 확인 |
| boolean | hasNext() | 다음 슬라이스 존재하는지 확인 |
| boolean | hasPrevious() | 이전 슬라이스 존재하는지 확인 |
| default Pageable | nextOrLastPageable() | 다음 Pageable을 반환.
단, 현재가 마지막 슬라이스라면 현재 Pageable을 반환 |
| Pageable | nextPageable() | 다음 Pageable 반환 |
| default Pageable | previousOrFirstPageable() | 이전 Pageable 반환.
단, 현재가 첫 슬라이스면 현재 Pageable 반환 |
| Pageable | previousPageable() | 이전 Pageable 반환 |

### Page

```java
public interface Page<T> extends Slice<T> {
	...
}
```

Page는 위와 같이 Slice를 상속받는다.

따라서 Slice에 존재하는 모든 메서드를 똑같이 가지게 된다.

그럼 Slice와 Page의 차이점이 뭘까?

일단, Page는 추가적으로 두가지 메서드를 더 가진다.

| 반환 타입 | 메서드 | 설명 |
| --- | --- | --- |
| long | getTotalElements() | 전체 데이터의 개수 반환 |
| int | getTotalPages() | 전체 페이지 수 반환 |

위와 같은 메서드를 위해 Page는 Slice와 달리 조회 쿼리 이후 전체 데이터 개수를 조회하는 쿼리가 한번 더 실행된다.

아래와 같은 테스트 코드로 쿼리가 어떻게 동작하는지 보자.

```java
@Test
    public void testPagination() throws Exception {
        // 데이터 추가
        userRepository.save(new UserModel("member1@example.com", "password1"));
        userRepository.save(new UserModel("member2@example.com", "password2"));
        userRepository.save(new UserModel("member3@example.com", "password3"));
        userRepository.save(new UserModel("member4@example.com", "password4"));
        userRepository.save(new UserModel("member5@example.com", "password5"));

        // PageRequest 설정: 두 번째 페이지, 페이지 당 2개의 항목
        PageRequest pageRequest = PageRequest.of(1, 2);

        // Page 조회
        Page<UserModel> page = userRepository.findAll(pageRequest);

        // Slice 조회
        Slice<UserModel> slice = userRepository.findAllBy(pageRequest);
				
				...
		}
```

### Page 조회

```sql
-- 데이터 조회 쿼리
SELECT usermodel0_.user_id AS user_id1_0_, usermodel0_.email AS email2_0_, usermodel0_.password AS password3_0_ 
FROM users usermodel0_ 
ORDER BY usermodel0_.user_id ASC LIMIT 2 OFFSET 2;

-- 전체 행 수 계산 쿼리
SELECT COUNT(usermodel0_.user_id) AS col_0_0_ 
FROM users usermodel0_;
```

### Slice 조회

```sql
-- 데이터 조회 쿼리
SELECT usermodel0_.user_id AS user_id1_0_, usermodel0_.email AS email2_0_, usermodel0_.password AS password3_0_ 
FROM users usermodel0_ 
ORDER BY usermodel0_.user_id ASC LIMIT 3 OFFSET 2;
```

위의 쿼리문과 같이 Page는 데이터 조회 쿼리 이후 전체 데이터의 개수를 한번 더 조회하는 것을 볼 수 있다.

# Page와 Slice 중 어떤 것을 써야할까?

그렇다면 우리는 Page와 Slice 중 어떤 것을 사용해야 할까?

Slice는 전체 데이터의 개수를 조회하지 않고, 다음 슬라이스가 존재하는지 정도만 알 수 있다.

따라서 Slice는 인스타, 유튜브 등에서 사용되는 것과 같이 무한 스크롤을 구현할 때 유용할 것이다.

또한, Slice는 Page에 비해 쿼리가 하나 덜 날아가기 때문에 데이터 양이 많을수록 Slice를 사용하는 것이 성능상 유리할 것이다.

하지만, 전체 페이지 수를 알 수 없기때문에 사용자가 어느정도의 데이터를 봤는지, 끝까지 얼마나 남았는지와 같은 정보를 알 수 없다는 단점도 존재한다.

반면, Page는 전체 데이터 개수를 조회하기 때문에 전체 페이지 개수, 전체 데이터 개수가 필요한 경우에 사용하면 좋을 것이다.

# 추가 : List로 페이지 받기

위의 Page, Slice 말고 List를 이용해서도 페이지를 구현할 수 있긴하다.

하지만 당연하게도 불편한 점이 많다.

- 전체 데이터 크기 정보 없음
    - List는 Page와 같이 전체 데이터 크기, 총 페이지 수 등의 정보를 제공하지 않는다.
- 직접적인 페이징 구현
    - 개발자가 직접 쿼리를 제어하고, 필요한 정보를 수동으로 계산해야 한다.
- 성능 고려 필요
    - 대량의 데이터를 다룰 때 다양한 작업을 수동으로 처리해야하기 때문에 성능 문제가 발생할 수 있다.

## List로 페이징 구현

List로 페이징을 구현하면 개발자가 직접 쿼리문을 작성하여 페이징 관련 로직을 관리해야한다.

```java
public interface PostRepository extends JpaRepository<PostModel, Integer> {
    @Query("SELECT p FROM PostModel p ORDER BY p.postId ASC")
    List<PostModel> findAllPostsWithPagination(@Param("offset") int offset, @Param("limit") int limit);
}
```

위와 같이 개발자가 직접 OFFSET과 LIMIT을 사용하여 직접적으로 페이징을 구현해야 한다.

그럼 이걸 서비스에선 어떻게 사용하는가?

```java
@Service
public class PostService{
	@Autowired
	private PostRepository postRepository;
	
	public List<PostModel> getPostsWithPagination(int page, int size){
		int offset = page * size;
		int limit = size;
		
		return postRepository.findAllPostsWithPagination(offset, limit);
	}
}
```

## List의 장단점

- 장점
    - 개발자가 직접 OFFSET과 LIMIT을 설정해서 페이징을 제어할 수 있는 것이 장점이 될 수 있다.
- 단점
    - 전체 데이터 크기, 전체 페이지 수 등을 제공하지 않아 추가 로직이 필요하다.
    - 대량의 데이터를 처리할 때 성능 문제가 발생할 수 있다.
    - Page, Slice에서 제공하는 다양한 페이징 관련 기능을 사용할 수 없다.

# 결론

스프링에서는 Page와 Slice와 같은 유용한 인터페이스를 제공한다.

이를 이용해 우리는 페이징을 간단하고 편리하게 구현할 수 있다.

우리가 게시판과 같이 총 데이터 개수가 필요한 환경에서는 Page를,

모바일 앱과 같이 총 데이터 개수가 필요없는 환경에서는 Slice를 활용하여 적절하게 사용하면 될 것이다.

List를 페이징에 사용한다면 모든 것을 수동으로 해야하므로 아주 귀찮고 시간이 오래 걸릴 수 있다.

따라서 대부분의 경우에는 Page와 Slice를 사용하는 것이 좋다.

만약 직접적인 OFFSET과 LIMIT에 직접적인 제어가 필요한 경우라면 List가 적절한 선택지가 될 수 있다.