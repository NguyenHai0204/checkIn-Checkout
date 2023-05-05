package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSheetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSheetDTO.class);
        TimeSheetDTO timeSheetDTO1 = new TimeSheetDTO();
        timeSheetDTO1.setId(1L);
        TimeSheetDTO timeSheetDTO2 = new TimeSheetDTO();
        assertThat(timeSheetDTO1).isNotEqualTo(timeSheetDTO2);
        timeSheetDTO2.setId(timeSheetDTO1.getId());
        assertThat(timeSheetDTO1).isEqualTo(timeSheetDTO2);
        timeSheetDTO2.setId(2L);
        assertThat(timeSheetDTO1).isNotEqualTo(timeSheetDTO2);
        timeSheetDTO1.setId(null);
        assertThat(timeSheetDTO1).isNotEqualTo(timeSheetDTO2);
    }
}
