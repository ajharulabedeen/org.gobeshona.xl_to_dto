package org.gobeshona.ExcelDto.excelTest;
import java.lang.reflect.Method;

public class MethodInvoker {
    public static Object invokeMethod(Object object, String methodName) throws Exception {
        Class<?> objectClass = object.getClass();
        Method method = objectClass.getMethod(methodName);
        return method.invoke(object);
    }

    public static void invokeMethod(Object object, String methodName, Object parameter) throws Exception {
        Class<?> objectClass = object.getClass();
//        Method method = objectClass.getMethod(methodName, parameter.getClass());
        Method method = objectClass.getMethod(methodName, parameter.getClass());
        method.invoke(object, parameter);
    }

    public static void main(String[] args) {
        // Example usage
        MyClass2 myObject = new MyClass2();
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setRoll(100);
        try {
            Object result = invokeMethod(studentDTO, "getRoll");
            System.out.println("Method invocation result: " + result);
        } catch (Exception e) {
            System.out.println("Error invoking method: " + e.getMessage());
        }
    }
}

class MyClass2 {
    public String getValue() {
        return "Hello, World!";
    }
}
