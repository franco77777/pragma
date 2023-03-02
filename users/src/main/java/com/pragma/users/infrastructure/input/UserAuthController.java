package com.pragma.users.infrastructure.input;

import com.pragma.users.application.handler.IObjectHandler;
import com.pragma.users.application.request.AuthenticateRequestDto;
import com.pragma.users.application.response.UserResponseDto;
import com.pragma.users.domain.model.AuthenticationModel;
import com.pragma.users.infrastructure.exception.InfrastructureException;
import com.pragma.users.infrastructure.output.entity.TokenDto;
import com.pragma.users.infrastructure.output.entity.UserEntity;
import com.pragma.users.infrastructure.output.mapper.IObjectEntityMapper;
import com.pragma.users.infrastructure.output.repository.IUserRepository;
import com.pragma.users.infrastructure.output.services.UserService;
import com.pragma.users.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserAuthController {
    private final IObjectHandler objectHandler;
    private final JwtService jwtService;
    private final UserService userService;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IObjectEntityMapper objectEntityMapper;


    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(
            @RequestBody @Validated AuthenticateRequestDto request
    ) {
        UserResponseDto user = objectHandler.userExists(request.getEmail(), request.getPassword());
        UserEntity result  = objectEntityMapper.responseToEntity(user);
        return ResponseEntity.ok(userService.authenticate(result));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validateUser(@RequestParam String token){
        TokenDto tokenDto = jwtService.validate(token);
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/user")
    public ResponseEntity<UserEntity> getUser(){
        UserEntity user = userRepository.findByEmail("franco@gmail.com124").orElseThrow();
        System.out.println("sout res");
        System.out.println(user);
        return ResponseEntity.ok(user);
    }


}