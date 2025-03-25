package co.com.consumer.api.security.mapper;

import co.com.consumer.api.security.dto.UserAppDetails;
import co.com.consumer.model.userapp.UserApp;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppMapper {

    private final ModelMapper modelMapper;

    public UserApp toEntity(UserAppDetails userAppDetails) {
        return modelMapper.map(userAppDetails, UserApp.class);
    }

    public UserAppDetails toDetails(UserApp userApp) {
        return modelMapper.map(userApp, UserAppDetails.class);
    }
}
