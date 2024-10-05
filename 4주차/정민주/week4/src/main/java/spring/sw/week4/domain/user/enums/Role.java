package spring.sw.week4.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN("0"),
    USER("1");

    private final String roleName;
}
