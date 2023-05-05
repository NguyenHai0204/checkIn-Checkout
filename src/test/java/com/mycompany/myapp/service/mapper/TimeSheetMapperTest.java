package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeSheetMapperTest {

    private TimeSheetMapper timeSheetMapper;

    @BeforeEach
    public void setUp() {
        timeSheetMapper = new TimeSheetMapperImpl();
    }
}
