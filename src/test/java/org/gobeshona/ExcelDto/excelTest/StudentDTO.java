package org.gobeshona.ExcelDto.excelTest;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class StudentDTO {
    private int studentId;
    private int roll;
    private String name;

    public StudentDTO(int studentId, String name, int roll) {
        this.studentId = studentId;
        this.name = name;
        this.roll = roll;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getRoll() {
        return roll;
    }

    public static List<StudentDTO> generateStudentList(int numberOfStudent) {
        List<StudentDTO> students = new ArrayList<>();

        for (int i = 1; i <= numberOfStudent; i++) {
            StudentDTO student = new StudentDTO(i, "Student " + i, i + 100);
            students.add(student);
        }

        return students;
    }

    public String invokeGetterMethod(int cellCount) {
        String methodName = "get" + capitalizeFirstLetter(getPropertyName(cellCount));

        try {
            Method getterMethod = getClass().getMethod(methodName);
            Object result = getterMethod.invoke(this);
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object invokeGetterMethod(String head) {
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if (isGetterMethod(method)) {
                String methodName = method.getName();
                String propertyName = methodName.substring(3).toLowerCase();
                if (propertyName.equals(head.toLowerCase())) {
                    try {
                        return method.invoke(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

    private boolean isGetterMethod(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get") && !methodName.equals("getClass")
                && method.getParameterCount() == 0 && !void.class.equals(method.getReturnType())) {
            return true;
        }
        return false;
    }

    private String capitalizeFirstLetter(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private String getPropertyName(int cellCount) {
        switch (cellCount) {
            case 0:
                return "studentId";
            case 1:
                return "name";
            case 2:
                return "roll";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", roll=" + roll +
                '}';
    }

    public Type getInvokedReturnType(int cellCount) {
        String methodName = "get" + capitalizeFirstLetter(getPropertyName(cellCount));

        try {
            Method getterMethod = getClass().getMethod(methodName);
            return getterMethod.getGenericReturnType();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
