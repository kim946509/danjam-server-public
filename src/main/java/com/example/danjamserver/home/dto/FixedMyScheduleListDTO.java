package com.example.danjamserver.home.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FixedMyScheduleListDTO {

    @NotNull @Valid
    private List<FixedMyScheduleDTO> fixedMyScheduleDTOList;

}
