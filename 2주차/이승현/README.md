# 2024_Spring_Study

### 2주차 의존성 주입

우리가 스프링을 공부하다보면 아래와 같은 의존성 주입을 사용하지 말라는 말을 많이 듣는다.

```java
@Autowired
private B b;
```

위와 같은 의존성 주입법을 필드 주입이라고 한다.

그렇다면 왜 이런 주입법을 사용하지 말라고 하는걸까?

# 의존성 주입의 종류

의존성 주입의 종류에는 크게 3가지 방법이 있다.

1. 필드 주입 (Field Injection)
2. 수정자 주입 (Setter Injection)
3. 생성자 주입 (Constructor Injection)

여기서 필드 주입과 생성자 주입은 많이 들어봤을텐데 수정자 주입은 많이 들어보지 못했을 것이다.

적어도 나는 그랬다.

그럼 이 세가지 의존성 주입법에 대해 살펴보자.

## 1. 필드 주입 (Field Injection)

필드 주입법은 해당 클래스의 필드에 @Autowired 어노테이션을 붙이면 된다.

아래와 같이 간단하게 사용가능하다.

```java
@Component
class B {
	public funcB() {
			System.out.println("hello I'm B");
	}
}
```

```java
class A{
	@Autowired
	private B b;
	
	public funcA(){
		this.b.funcB;
		...
	}
}
```

A 클래스에서 @Autowired 를 사용하여 스프링에서 자동으로 B 객체를 주입해준다.

## 2. 수정자 주입 (Setter Injection)

수정자 주입은 Setter를 통해 필드의 값을 수정할 수 있는 특성을 이용한 주입법이다.

주입 받는 객체가 변경될 가능성이 있는 의존관계에서 주로 사용된다. (실제로는 굉장히 드문 경우라고 한다.)

클래스에 Setter method를 만들어 @Autowired 어노테이션을 붙이면 된다.

```java
class A{
	private B b;
	
	@Autowired
	public void setB(B b){
		this.b = b;
	}
	...
}
```

또한 우리는 Lombok으로 더욱 간단하게 사용할 수 있다.

```java
class A{
	@Setter(onMethod_={@Autowired})
	private B b;
}
```

## 3. 생성자 주입 (Constructor Injection)

생성자 주입은 우리가 잘 알 듯, 단일 생성자인 경우에는 @Autowired 어노테이션을 사용하지 않아도 된다. 하지만, 생성자가 2개 이상인 경우에는 해당 어노테이션을 붙여야 한다.

아래 예시를 보자.

```java
@Component
class A{
	private final B b;
	
	public A(B b){
		this.b = b;
	}
	
	public void funcA() {
		b.funcB();
	}
}
```

평소에는 위의 예시와 같이 생성자를 통해 직접 의존성을 주입하는 것이다.

하지만 생성자가 여러개 필요하다면

```java
@Component
class A {
    private final B b;

    // 기본 생성자
    public A() {
        this.b = null; // 기본 생성자에서는 b가 주입되지 않음
    }

    // 의존성을 주입받는 생성자
    @Autowired
    public A(B b) {
        this.b = b;
    }

    public void funcA() {
        if (b != null) {
            b.funcB();
        } else {
            System.out.println("B is not initialized");
        }
    }
}
```

위와 같이 의존성을 주입받는 생성자에 @Autowired 어노테이션을 붙여주는 것이다.

Lombok으로 작성한 생성자 주입

```java
@Component
@RequiredArgsConstructor
public class A {
	private final B b;
	
	...
}
```

## 왜 필드, 수정자 주입을 사용하지 말라고 하는가?

그렇다면 왜 필드, 수정자 주입을 피하고 생성자 주입 방법을 쓰라고 할까?

### 필드 주입

필드 주입을 사용하면 구현이 아주 간단하고, 에러가 생기면 빌드에서 예외가 발생한다.

그래서 언뜻보면 아주 좋은 방법이라고 생각된다.

하지만 아래와 같은 경우를 보자.

```java
A a = new A();
a.funcA();
```

여기서 A클래스는 B클래스를 주입받아야 동작할 수 있다. 하지만, 해당 코드는 new로 생성되어 B 객체가 자동으로 주입되지 않아서 NullPointerException이 발생하게 된다.

### 수정자 주입

위에서 언급했듯이 수정자 주입 방식은 주입받는 객체가 변경될 가능성이 있는 경우에 사용한다.

하지만 실제 개발에서는 변경이 필요한 상황이 거의 없고, 수정자 주입을 사용하면 불필요하게 객체의 수정 가능성을 열어두게 되는 것이다.

이는 객체 지향 프로그래밍의 개발 원칙 중 OCP(Open-Closed Principal) 개방-폐쇄 원칙을 위반하게 된다고 한다.

### OCP(Open-Closed Principal) 개방-폐쇄 원칙이란?

소프트웨어 개체 (클래스, 모듈, 함수 등)는 확장에 대해서는 열려있고, 수정에 대해서는 닫혀있어야 한다는 프로그래밍 원칙이다.

즉, `기존의 코드를 변경하지 않으면서, 기능을 추가할 수 있도록 설계가 되어야 한다`는 것이다.

따라서, 수정자 주입이 아닌 생성자 주입을 사용함으로써 변경의 가능성을 배제하고 불변성을 보장하는 것이 좋다고 한다.

## 생성자 주입을 사용하는 이유

### 1. SRP (단일 책임의 원칙)을 위반할 확률이 줄어든다.

우리가 작성하는 클래스는 하나의 비즈니스 로직에 집중되어 있어야 한다.

하지만 필드 주입 방식을 사용하면 아주 간단하게 사용가능해서 하나의 클래스가 여러 기능을 담당하게 만들기도 쉽다는 것이다.

그러나 생성자 주입을 사용하면 생성자 파라미터에 사용하고자 하는 필드를 모두 넣어야하기 때문에 코드가 길어지고, 경각심이 생겨 더 조심해서 코딩을 하게 된다는 것이다.

근데 이 말은 Lombok을 사용하면 생성자 주입이 더 간단해지는 느낌이 있어서 일단 넘어가자.

### 2. 필드에 final을 선언할 수 있다.

필드, 수정자 주입 방식은 final을 선언할 수 없다. 필드에 fina을 붙이기 위해서는 클래스의 객체가 생성될 때 final 이 붙은 필드를 반드시 초기화해야 한다.

하지만 필드, 수정자 주입 방식은 객체가 먼저 생성되고 해당 필드에 의존성 주입이 진행되므로 final을 붙일 수 없다.

그러나 생성자 주입은 필드를 파라미터로 받는 생성자를 통해 객체가 생성될 때 의존성 주입이 일어나기 때문에 final을 붙일 수 있는 것이다.

이를 통해 우리는 해당 필드 값이 변경되는 것을 막고 불변성을 가지도록 하는 것이다.

### 3. 순환 참조를 발견하기 쉽다.

아래와 같은 필드 주입 예시를 보자.

```java
@Service
public class A{
	@Autowired
	private B b;
	
	public void funcA() {
		b.funcB();
	}
}
```

```java
@Service
public class B{
	@Autowired
	private A a;
	
	public void funcB() {
		a.funcA();
	}
}
```

위와 같은 코드를 실행하면 한쪽의 코드가 실행되면 new A(new B(new A(new B(new A …))) 와 같은 상황이 반복되면서 결국 StackOverflowError가 발생한다.

해당 문제는 둘 중 하나가 호출되기 전에는 찾지 못한다.

즉, 프로그램이 실행될때는 오류가 발생하지 않고 정상적으로 실행이되다가 어느 한 쪽이 사용될 때 오류가 발생한다는 것이다.

반면에 생성자 주입은 어떨까?

```java
@Service
public class A{
	private final B b;
	
	public A(B b){
		this.b = b;
	}
	
	public void funcA() {
		b.funcB();
	}
}
```

```java
@Service
public class B{
	private final A a;
	
	public B(A a){
		this.a = a;
	}
	
	public void funcB() {
		a.funcA();
	}
}
```

위와 같이 생성자를 통해 의존성을 주입하면 아래와 같이 프로그램이 구동조차 되지 않고 예외가 발생한다.

```java
Description:
The dependencies of some of the beans in the application context form a cycle:
┌─────┐
|  a defined in file [~~~/A.class]
↑     ↓
|  b in file [~~~/B.class]
└─────┘
```

그럼 왜 이렇게 의존성 주입 방법에 따라 차이가 오류를 발견하는 시점의 차이가 생길까?

이는 빈 생성 시기가 차이나기 때문이다.

필드, 수정자 주입 방식은 빈을 먼저 생성하고 어노테이션이 붙은 필드에 해당하는 빈을 찾아서 주입하는 방식이다.

즉, 빈을 먼저 생성하고 필드를 주입하기 때문에 빈 객체를 생성한 시점에서는 순환 참조 여부를 알 수 없다.

그렇지만 생성자 주입 방식은 생성자로 객체를 생성하는 시점에 필요한 빈을 주입한다. 즉, 더 쉽게 말하자면 빈 객체를 생성하는 시점에 생성자의 파라미터 빈 객체를 찾아서 먼저 주입을 하고, 주입받은 객체를 이용하여 생성한다.

이런 이유로 순환 참조는 생성자 주입 방식에서만 문제가 생기는 것이다.

### 4. 테스트 코드 작성이 편하다.

만약 필드 주입, 수정자 주입으로 의존성을 주입했다면 Mockito를 이용해 목킹을 한 후 테스트를 진행해야 한다고 한다.

```java
@Component
class B {
    public void funcB() {
        System.out.println("this is b");
    }
}

@Component
class A {
    @Autowired
    private B b;

    public void funcA() {
        b.funcB();
    }
}
```

```java
public class ATest {

    @Mock //B 클래스 모킹
    private B b;

    @InjectMocks //A 클래스에 B 클래스 주입
    private A a;

    @Test
    void testFuncA() {
        // Mockito가 위의 어노테이션을 처리하도록 지시
        MockitoAnnotations.openMocks(this);
        
        // 테스트를 위해 메서드 호출
        a.funcA();
        
        // B 클래스의 funcB 메서드가 호출되었는지 확인
        verify(b).funcB();
    }
}
```

해당 코드는 자세하게 보지 않고 넘어가겠다. 딱봐도 귀찮아 보이지 않는가?

그렇다면 생성자 주입을 사용한 클래스의 테스트는 어떻게 진행될까?

```java
@Component
class B {
    public void funcB() {
        System.out.println("this is b");
    }
}

@Component
class A {
    private final B b;

    public A(B b) {
        this.b = b;
    }

    public void funcA() {
        b.funcB();
    }
}
```

```java
public class ATest {

    @Test
    public void testFuncA() {
        B b = new B();
        A a = new A(b);
        a.funcA();
    }
}

```

차이가 확실하지 않나?

위와 같이 생성자 주입 방식은 단순히 원하는 객체를 생성하고 생성자에 넣어주기만 하면 된다.

이와 같이 생성자 주입을 사용하는 이유에 대해 알아보았다.

정리하자면,

1. 단일 책임 원칙 위반 방지
2. final 선언으로 인한 불변성
3. 순환 참조 방지
4. 테스트 코드 작성 용이

가 될 것이다.