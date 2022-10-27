package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.MemberDTO;
import com.soongkordan.dothejib.controller.dto.MemberDTO.MemberRequest;
import com.soongkordan.dothejib.controller.dto.TokenDTO;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.soongkordan.dothejib.controller.dto.MemberDTO.*;
import static com.soongkordan.dothejib.controller.dto.TokenDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/signUp",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> signUp(@RequestBody MemberRequest request){
        return ResponseEntity.ok()
                .body(new CommonResponse<Response>(authService.signUp(request)));
    }

    @PostMapping(path = "/login",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> login(@RequestBody MemberRequest request){
        return ResponseEntity.ok()
                .body(new CommonResponse<TokenDto>(authService.login(request)));
    }

    @PostMapping(path = "/reissue",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> reissue(@RequestBody TokenRequestDto request){
        return ResponseEntity.ok()
                .body(new CommonResponse<TokenDto>(authService.reissue(request)));
    }

}
