package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.TimeSheet;
import com.mycompany.myapp.service.dto.TimeSheetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeSheet} and its DTO {@link TimeSheetDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeSheetMapper extends EntityMapper<TimeSheetDTO, TimeSheet> {}
