package org.gobeshona.ExcelDto.excelTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelToDto_DTO {
    private BigDecimal NOTIFICATION_ID;
    private String USERNAME;
    private String NAME;
    private String DESIGNATION;
    private String BRANCH_ID;
    private String BRANCH_NAME;
    private String SEEN;
    private String SEEN_DATE;
}
