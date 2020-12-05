package com.sbakic.usermanagement.service.mapper;

import com.sbakic.usermanagement.domain.ApplicationUser;
import com.sbakic.usermanagement.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = AuthorityMapper.class,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  ApplicationUser toUser(UserDto userDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  void mapToUser(@MappingTarget ApplicationUser applicationUser, UserDto userDto);

  @Mapping(target = "password", ignore = true)
  UserDto toDto(ApplicationUser applicationUser);

}
