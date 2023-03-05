package com.pragma.users.infrastructure.input;
import com.pragma.users.application.handler.IObjectHandler;
import com.pragma.users.application.mapper.IObjectRequestMapper;
import com.pragma.users.application.request.UserRequestDto;
import com.pragma.users.application.response.UserResponseDto;
import com.pragma.users.infrastructure.output.entity.TokenDto;
import com.pragma.users.infrastructure.output.services.UserService;
import com.pragma.users.infrastructure.output.utils.AuthorityName;
import com.pragma.users.infrastructure.output.utils.SquareFeingClient;
import com.pragma.users.infrastructure.output.utils.SquareService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/user/register")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserRegisterController {
    private final UserService userService;
    private final IObjectHandler objectHandler;
    private final IObjectRequestMapper objectRequestMapper;
    private final SquareService squareService;



    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){

        System.out.println("soy security");
        System.out.println(SecurityContextHolder.getContext());
        return ResponseEntity.ok(objectHandler.getAllObjects());
    }

    @PostMapping("/client")
    public ResponseEntity<TokenDto> saveClient(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto responseDto =objectHandler.saveObject(userRequestDto, AuthorityName.ROLE_CLIENT);
        TokenDto token = userService.registerToken(objectRequestMapper.toUserEntity(responseDto));

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<TokenDto> saveAdmin(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto responseDto = objectHandler.saveObject(userRequestDto, AuthorityName.ROLE_ADMIN);
        TokenDto token = userService.registerToken(objectRequestMapper.toUserEntity(responseDto));
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

   // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/owner")
    public ResponseEntity<UserResponseDto> saveOwner(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto responseDto = objectHandler.saveObject(userRequestDto, AuthorityName.ROLE_OWNER);
        //TokenDto token = userService.registerToken(objectRequestMapper.toUserEntity(responseDto));
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

   //@PreAuthorize("hasRole('OWNER')")

    @PostMapping("/employee/{restaurantId}")
    public ResponseEntity<String> saveEmployer(@RequestBody @Valid UserRequestDto userRequestDto, @PathVariable("restaurantId") Long restaurantId){
        UserResponseDto responseDto = objectHandler.saveObject(userRequestDto, AuthorityName.ROLE_EMPLOYEE);
        Long userId = responseDto.getId();
       try {
           String OwnerId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
           Boolean squareResponse = squareService.createRestaurantEmployee(Long.parseLong(OwnerId),restaurantId,responseDto.getId());
           return new ResponseEntity<>("employee: " + responseDto.getEmail() + " created", HttpStatus.CREATED);
       } catch (Exception e) {
           objectHandler.deleteUser(userId);
           return new ResponseEntity<>("bad request: "+e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }


}
