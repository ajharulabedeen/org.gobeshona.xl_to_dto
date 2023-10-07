package org.gobeshona.ExcelDto.excelTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PrivateFieldLister {
    public static void main(String[] args) {
//        Class<MyClass> clazz = MyClass.class;
        Class<NotificationReportDTO> clazz = NotificationReportDTO.class;

        List<Field> privateFields = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isPrivate(field.getModifiers())) {
                privateFields.add(field);
            }
        }

        for (Field field : privateFields) {
            System.out.println(field.getName());
        }
    }
}