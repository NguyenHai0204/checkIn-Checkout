package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSheet.class);
        TimeSheet timeSheet1 = new TimeSheet();
        timeSheet1.setId(1L);
        TimeSheet timeSheet2 = new TimeSheet();
        timeSheet2.setId(timeSheet1.getId());
        assertThat(timeSheet1).isEqualTo(timeSheet2);
        timeSheet2.setId(2L);
        assertThat(timeSheet1).isNotEqualTo(timeSheet2);
        timeSheet1.setId(null);
        assertThat(timeSheet1).isNotEqualTo(timeSheet2);
    }
}
