package spring.sw.week4.common.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.sw.week4.common.jwt.dto.AccessTokenDto;
import spring.sw.week4.common.jwt.service.JwtFacadeService;


@Tag(name = "JWT", description = "JWT Api")
@RestController
@AllArgsConstructor
public class JwtController {
    private final JwtFacadeService jwtFacadeService;

    @Transactional
    @Operation(summary = "request new access Token", description = "어세스 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 : 헤더에 refreshToken을 넣기 \n",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccessTokenDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping("/jwt/update/access/token")
    public AccessTokenDto updateAccessToken(HttpServletRequest request){
        return jwtFacadeService.updateAccessToken(request);
    }



}