package spring.study.week3.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.study.week3.common.dto.SeedDto;
import spring.study.week3.common.exception.collections.BindingErrors;
import spring.study.week3.domain.facade.FacadeService;
import spring.study.week3.domain.user.dto.DummyUserRspDto;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {
    private final FacadeService facadeService;

    @PostMapping("/testing/testingUser")
    public ResponseEntity<?> createTestingUser (@RequestBody @Valid SeedDto seedDto, BindingResult bindingResult) throws IOException {
        handleBindingErrors(bindingResult);
        List<DummyUserRspDto> userRspDtos = facadeService.createTestingUser(seedDto);
        return ResponseEntity.ok(userRspDtos);
    }

    public void handleBindingErrors (BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BindingErrors();
        }
    }

}

///v1/testing/testingUser 에 POST 방식으로 body 에 seed 를 담아서 보낼 시 ,
// 해당 seed에 해당하는 Testing User가 100개 생성되어 DB에 Insert 된다.
