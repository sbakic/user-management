package com.sbakic.usermanagement.service.mapper;

import com.sbakic.usermanagement.domain.Authority;
import com.sbakic.usermanagement.service.dto.AuthorityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

  AuthorityDto toDto(Authority authority);

}
