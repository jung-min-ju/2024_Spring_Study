package spring.sw.week4.common.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenSubAndRoleDto {
    private String accountId; // account ID
    private String role; //해당 토큰 사용자의 권한
}
