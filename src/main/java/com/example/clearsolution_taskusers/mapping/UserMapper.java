package com.example.clearsolution_taskusers.mapping;


import com.example.clearsolution_taskusers.dto.UserCreateRequest;
import com.example.clearsolution_taskusers.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public User convertCreateRequestToEntity(UserCreateRequest request) {
        return modelMapper.map(request, User.class);
    }

}
