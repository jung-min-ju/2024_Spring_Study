package spring.study.week3.domain.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DummyUserRspDto {
    private String email;
    private String password;
}
