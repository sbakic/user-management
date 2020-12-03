package com.sbakic.usermanagement.service.mapper;

import com.sbakic.usermanagement.domain.ApplicationUser;
import com.sbakic.usermanagement.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthorityMapper.class)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  ApplicationUser toUser(UserDto userDto);

}
