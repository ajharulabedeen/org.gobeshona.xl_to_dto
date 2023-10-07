package org.gobeshona.ExcelDto.excelTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/**
 * Seniors will be able to see how many of his subordinates have seen particular notice.
 */
public class NotificationReportDTO {
    private String NOTIFICATION_ID;
    private String USERNAME;
    private String NAME;
    private String DESIGNATION;
    private String BRANCH_ID;
    private String BRANCH_NAME;
    private String SEEN;
    private String SEEN_DATE;
}
