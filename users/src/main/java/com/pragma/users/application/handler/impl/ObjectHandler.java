package com.pragma.users.application.handler.impl;

import com.pragma.users.application.handler.IObjectHandler;
import com.pragma.users.application.mapper.IObjectRequestMapper;
import com.pragma.users.application.mapper.IObjectResponseMapper;
import com.pragma.users.application.request.UserRequestDto;
import com.pragma.users.application.response.UserResponseDto;
import com.pragma.users.domain.api.IObjectServicePort;
import com.pragma.users.domain.model.UserModel;
import com.pragma.users.infrastructure.output.utils.AuthorityName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ObjectHandler  implements IObjectHandler {
    private final IObjectServicePort objectServicePort;
    private final IObjectRequestMapper objectRequestMapper;
    private final IObjectResponseMapper objectResponseMapper;
    @Override
    public UserResponseDto saveObject(UserRequestDto userRequestDto, AuthorityName role) {
        UserModel userModel =  objectRequestMapper.toUserModel(userRequestDto);

       return objectResponseMapper.toUserResponseDto(objectServicePort.saveObject(userModel,role));


    }

    @Override
    public List<UserResponseDto> getAllObjects() {
        return  objectResponseMapper.toUserResponseList(objectServicePort.getAllObjects());
    }

    @Override
    public boolean emailExists(String email) {
        return objectServicePort.emailExists(email);
    }

    @Override
    public UserResponseDto userExists(String email, String password) {
        return objectResponseMapper.toUserResponseDto(objectServicePort.userExists(email,password));
    }

    @Override
    public void deleteUser(Long id) {
         objectServicePort.deleteUser(id);
    }

}
