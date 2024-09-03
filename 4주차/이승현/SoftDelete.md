이번에 소공을 하면서 삭제와 관련된 이야기를 한 적이 있는데

관련된 자료들을 찾다가 우리가 얘기하던 주제와는 관련이 없지만 Soft Delete라는 것을 알게 되었다.

삭제에는 크게 두가지 종류가 있다고 한다.

- Hard Delete
- Soft Delete

# Hard Delete, Soft Delete

## Hard Delete

Hard Delete는 물리적인 삭제이다.

즉, 데이터베이스에서 직접 데이터를 삭제하는 것이다.

따라서 데이터를 한번 삭제한다면 다시 복구하기는 어렵다.

우리가 스프링에서 userRepository.delete(user)와 같은 삭제가 Hard Delete에 해당한다고 볼 수 있다.

## Soft Delete

Soft Delete는 Hard Delete와 반대로 물리적으로 삭제되는 것이 아닌, 엔티티에 Flag, State 등의 필드를 추가하여 삭제 여부를 판단하는 것이다.

즉, 실제로 데이터베이스에서 삭제하는 것이 아닌 삭제된 척을 하는 것이다.

하지만 실제로 삭제되는 것이 아니므로 데이터베이스의 사용량이 늘어나기는 할 것이고, 조회 시 삭제 여부도 판단해야 하므로 조회 성능도 저하될 수 있고, 개발자의 실수로 삭제된 데이터가 사용자에게 보여질 수 있다.

그렇다면 우리는 왜 Soft Delete를 쓸까?

삭제된 데이터를 사용해야 하는 경우가 있을 수 있고,

사용자의 실수로 데이터를 삭제한다면 Hard Delete는 삭제된 데이터의 복원이 어렵기 때문이다.

# Soft Delete의 구현

## @SQLDelete

@SQLDelete는 Delete쿼리가 발생할 때, 개발자가 정의한 특정 쿼리로 바꿔서 실행해준다.

아래 예시를 보자.

```java
@Entity
@Table(name = "user")
@Getter
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

		//생략..
```

위의 예시를 보면 Delete 쿼리가 발생할 때 delete = true 의 update 쿼리가 발생하도록 한다.

해당 쿼리문은 transaction이 종료되는 시점에 적용된다고 한다.

```java
Hibernate: UPDATE user SET deleted = true WHERE user_id = ?
```

위 코드를 가지고 삭제를 해보면 실제로 delete 가 아닌 update 쿼리가 발생하는 것을 알 수 있다.

## @When

@When은 Select 쿼리가 발생할 때, 개발자가 정의한 Where 구문이 추가되어 실행된다.

```java
@Entity
@Table(name = "user")
@Getter
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

		//생략..
```

위와 같은 코드가 있다면 해당 엔티티를 선택하는 Select 쿼리에 where deleted = false 구문이 추가되어 삭제되지 않은 데이터만 조회할 수 있는 것이다.

# @When의 문제점

위와 같은 방식으로 우리는 Soft Delete를 손쉽게 구현할 수 있었다.

하지만 이 방식의 문제가 하나도 없을까?

위와 같은 코드는 모든 Select에서 삭제된 데이터는 제외하게 된다.

우리가 Soft Delete를 구현한 이유는 삭제된 데이터를 이용하기 위해서이지만 위의 코드에서는 삭제된 데이터를 이용하기 어렵다.

즉, @When을 사용하면 편리하게 구현을 할 수 있지만, Soft Delete를 구현하면서 얻고자 하는 이점을 얻을 수 없기는 어렵다는 것이다.

실제로 김영한씨도 @When은 실무에서 사용하기 힘들다고 말한다.

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9521a837-a8bb-493a-a9a0-6fa605de0301/07d46231-aa6b-4cbb-a513-aed726a0f347/image.png)

따라서 우리는 위에서 설명한 @Where를 사용하지 않고 구현해야 할것이다.

# @Soft Delete

최근에 하이버네이트의 6.4 버전이 나오면서 Soft Delete를 어노테이션 하나로 퉁칠 수 있는 기능이 추가되었다고 한다.

해당 어노테이션이 생기고 그냥 엔티티에 @SoftDelete라는 어노테이션 하나만 붙이면 Soft Delete가 자동으로 처리되게 된다.

또한, 자동으로 boolean deleted 컬럼이 생기면서 기본으로 false를 하게 된다고 한다.

실제로 전에 연습으로 만들었던 프로젝트의 user 엔티티에 해당 어노테이션을 적용 시켜봤다.

실제로 user 엔티티에는 deleted 라는 필드가 존재하지 않지만

실제 데이터베이스를 조회해보면 deleted 라는 컬럼이 존재하는 것을 볼 수 있다.

또한 삭제를 하면 자동으로 update 구문이 나가게된다.

```java
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SoftDelete
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "nickname",unique = true)
    private String nickname;
```

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9521a837-a8bb-493a-a9a0-6fa605de0301/5170cde3-ecf9-4922-9953-40d9a684e4be/image.png)

## 꿀팁

### 1. 컬럼명 변경

만약 우리가 삭제된 것을 표시하는 컬럼인 deleted를 사용하고 싶지 않다면?

아래와 같이 컬럼명을 변경해줄 수 있다.

```java
@SoftDelete(columnName = "IS_DEL") 

//이렇게 쓰자
@Column(name = "IS_DEL", insertable = false, updatable = false)
private boolean isDelete;
```

해당 컬럼은 하이버네이트에서 직접 제어하기 때문에 개발자가 직접 삽입할 수 없도록 insertable, updatable을 false 로 지정하는 것이다.

## 2. 자식 삭제?

```java
@SoftDelete
public class User {
	
	@OneToMany(maapedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserAddress> addresses = new ArrayList<>();
	
	...
}
```

```java
public class UserAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String addressName;
	
	private String addressDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
}
```

이런 양방향으로 선언된 부모-자식 관계가 있을 때,

부모를 삭제하게되면 자식은 hard delete 처리되게 된다.

자식도 Soft Delete 하고 싶다면?

그냥 자식에도 Soft Delete 어노테이션을 붙이면 된다.

```java
@SoftDelete
public class UserAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String addressName;
	
	private String addressDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
}
```

너무 간단하다…

이 기능이 나온지 얼마 되지 않아 관련된 자료가 많이 없지만,

실무에서도 Soft Delete를 많이 쓴다고 하니 나중에 기회가 된다면 써보는 것도 좋을 것 같다.