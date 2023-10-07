package org.gobeshona.ExcelDto.excelTest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class ObjectMetaData {
    private String fieldName;
    private String dataType;
    private String getterMethodName;
}
