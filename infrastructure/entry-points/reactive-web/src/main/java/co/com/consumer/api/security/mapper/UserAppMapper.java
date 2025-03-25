package co.com.consumer.api.security.mapper;

import co.com.consumer.api.security.dto.UserAppData;
import co.com.consumer.model.userapp.UserApp;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppMapper {

    private final ModelMapper modelMapper;

    public UserApp toEntity(UserAppData userAppData) {
        return modelMapper.map(userAppData, UserApp.class);
    }

    public UserAppData toData(UserApp userApp) {
        return modelMapper.map(userApp, UserAppData.class);
    }
}
