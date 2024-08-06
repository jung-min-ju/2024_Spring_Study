# 2024_Spring_Study

### 1주차 테스트 코드

아직 시큐리티 부분을 제대로 파지 못한 관계로 딱히 새롭게 배운 점은 없는 것 같다.

사실 지금까지 스프링을 하면서 테스트 코드를 제대로 작성해본 적이 거의 없는데,

이번에 새로 공부하면서 테스트 코드를 작성하는 법도 새롭게 공부할 수 있었다.

테스트 코드에는 다양한 패턴이 있는데,

그 중 given-when-then 패턴이 있다.

## given-when-then

이 패턴은 테스트코드를 세 단계로 구분해서 작성하는 방식이다.

각 단계는

given - 테스트 실행을 준비하는 단계

when - 테스트를 진행하는 단계

then - 테스트 결과를 검증하는 단계

로 나뉘어 진다.

예를 들어, 카페에서 새로운 메뉴를 저장하는 코드를 테스트한다고 하자.

```java
@DisplayName("새로운 메뉴를 저장한다.")
@Test
public void saveMenuTest(){
	//1.given : 메뉴를 저장하기 위한 준비 과정
	final String name = "아메리카노";
	final int price = 2000;
	final Menu americano = new Menu(name, price);
	
	//2.when : 실제로 메뉴를 저장
	final long savedId = menuService.save(americano);
	
	//3.then : 메뉴가 잘 추가되었는지 검증
	final Menu savedMenu = menuService.findById(savedId).get();
	assertThat(savedMenu.getName()).isEqualTo(name);
	assertThat(savedMenu.getPrice()).isEqualTo(price);
}
```

위와 같이 세 단계로 나누어서 테스트를 진행할 수 있다.

근데 분명히 책에서나 다른 블로그들에서 본 바로는 assertThat만 사용해도 코드가 동작하는 것처럼 나오는데.

나는 Assertions.assertThat()으로 해줘야 사용이 된다.

이게 그냥 내 인텔리제이의 문제인건지 내가 무언가 빼먹은게 있는건지 아직도 잘 모르겠다.

혹시 해결방법을 아는 착한 사람은 제보 부탁한다.

## AssertJ

위 코드에서 사용했던 assertThat은 AssertJ의 메서드이다.

Assertions는 두가지가 존재한다.

하나는 JUnit 자체의 Assertions, 다른 하나는 바로 AssertJ의 Assertions이다.

그렇다면 왜 우리는 AssertJ를 사용할까?

### 1. 가독성

아래와 같은 예시를 보자.

```java
//JUnit
assertEquals(a, b);

//AssertJ
assertThat(a).isEqualTo(b);
```

예시와 같이 위의 JUnit 코드는 누가 실제 값이고, 누가 예상값인지 쉽게 예측할 수 없다.

하지만 아래의 AssertJ는 한눈에 봐도 a가 실제 값이고 b가 예상값이라는 것을 쉽게 유추할 수 있다.

### 2. 실패 원인 파악 용이

```java
//Junit
assertTrue(name.contains("o"));
```

```java
//실패 메시지
expected: <true> but was: <false>
Expected :true
Actual : false
```

JUnit으로 테스트를 진행하면 위와 같이 어디서 테스트를 실패했는지, 뭐때문에 실패했는지 알 수가 없다.

(이럴거면 테스트 왜 하냐?)

```java
//AssertJ
assertThat(name).contains("o");
```

```java
java.lang.AssertionError:
Expecting actual:
	"lsh"
to contain:
	"o"
```

위와 같이 AssertJ로 테스트를 진행하면 “lsh”라는 이름을 검증하는 과정에서 “o”가 포함되어야 하는데 포함되지 않아서 실패했다는 것을 알 수 있게 된다.

더 자세한 예시를 보자.

```java
Players players = new Players("승현", "원영", "민주", "승제", "선안", "길동");
List<String> winners = players.findWinners();

...

assertThat(winners).containsExactlyInAnyOrder("승현", "선안");
```

위의 테스트는 6명의 플레이어 중 승자를 찾는 것이다.

작성자는 승현과 선안이 우승할 것이라고 예상했다.

아래의 오류 메시지를 분석해보자.

```java
java.lang.AssertionErro:
Expecting actual:
	["승현", "길동"]
to contain exactly in any order:
	["승현", "선안"]
elements not found:
	["선안"]
and elements not expected:
	["길동"]
```

아주 보기 쉽게 출력해준다.

실제 우승자는 승현, 길동이다.

우리가 예측한 우승자는 승현, 선안이다.

선안은 우승자에 포함되지 않았다.

길동은 우승자가 아니라고 예상했지만 우승자가 맞았다.

이와 같이 AssertJ를 사용하면 실패 원인을 보다 더 자세하고 정확하게 파악할 수 있다.

### 3. 다양한 메서드 제공

아래 예시를 보자.

```java
//JUnit
assertTrue(winners.containsAll(List.of("승현", "선안")) && winners.size() == 2);
assertArrayEquals(winners.toArray(), new String[]{"승현", "선안"});
assertTrue(winners.containsAll(List.of("승현", "선안")));

// AssertJ
assertThat(winners).containsExactlyInAnyOrder("승현", "선안");
assertThat(winners).containsExactly("승현", "선안");
assertThat(winners).contains("승현", "선안");
```

위와 같이 같은 동작을 하는데 JUnit은 간단한 메서드만 지원을하기 때문에 비교를 위해 많은 작업들이 필요해진다.

반면에 AssertJ는 다양한 메서드를 지원해서 가독성도 좋아지고 통일성도 좋아지는 효과가 있다.

### 4. 메서드 체이닝

```java
assertThat("[TITLE] Hello, My Name is lsh")
        .isNotEmpty()
        .contains("[TITLE]")
        .containsOnlyOnce("ash")
        .doesNotStartWith("[ERROR]");

assertThat(score)
        .isPositive()
        .isGreaterThan(60)
        .isLessThanOrEqualTo(75);

```

위의 예시와 같이 메서드 체이닝을 통해 여러 테스트를 한 번에 진행할 수 있다.

위 코드를 JUnit으로 테스트한다면 그냥 짜증나서 맥북을 반으로 접어버릴 수도 있을 것이다.(사실 그정돈 아님)

이 외에도 아주 많은 이유들이 있겠지만, AssertJ를 안쓸 이유가 없는 것 같다.

가독성이 좋아지는 것이 아주아주 마음에 든다.

## JUnit

JUnit은 자바를 위한 단위 테스트 프레임워크이다.

단위 테스트는 코드가 의도대로 작동하는지 작은 단위로 검증하는 것을 의미한다.

이때 작은 단위는 보통 메서드가 된다고 한다.

JUnit에서는 테스트끼리 영향을 주지 않도록 각 테스트를 실행할 때마다 테스트를 위한 실행 객체를 만들고 테스트가 종료되면 해당 객체를 삭제한다.

JUnit을 이용하여 테스트코드를 작성한 예시를 보자.

```java
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest{
	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@BeforeEach
	public void mockMvcSetUp(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.build();
	}
	
	@AfterEach
	public void cleanUp(){
		memberRepository.deleteAll();
	}
	
	@DisplayName("getAllMembers: 아티클 조회에 성공)
	@Test
	public void getAllMembers() throws Exception{
		//given
		final String url = "/test";
		Member savedMember = memberRepository.save(new Member(1L,"홍길동");
		
		//when
		final ResultActions result = mockMvc.perform(get(url) 
		//perform()은 요청을 전송하는 역할을 한다. 결과로 ResultActions 객체를 받고 해당 객체는 반환값을 검증하고 확인하는 andExpect() 메서드를 제공한다.
			.accept(MediaType.APPLICATION_JSON);
			//accept()는 요청을 보낼 때 무슨 타입으로 응답을 받을지 결정하는 메서드.
		
		//then
		result.andExpect(status().isOk())
		//andExpect는 응답을 검증한다. isOk()를 사용하여 응답 코드가 200인지 확인.
			.andExpect(jsonPath("$[0].id").value(savedMember.getId()))
			//jsonPaht("$[0].${필드명}")은 json 응답의 값을 가져오는 역할. 0번째 배열에 있는 객체의 id, name을 가져와 비교한다.
			.andExpect(jsonPath("$[0].name").value(savedMember.getName()));
	}
}
```