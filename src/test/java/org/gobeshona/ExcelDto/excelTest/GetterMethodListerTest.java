package org.gobeshona.ExcelDto.excelTest;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GetterMethodListerTest {

    @Test
    public void testMethodInOrderCustomAnnotation() {
        Class<MyClass> clazz = MyClass.class;

        List<Method> getterMethods = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (isGetterMethod(method)) {
                getterMethods.add(method);
            }
        }

        // Sort the getter methods by the order specified in the GetterOrder annotation
        getterMethods.sort(Comparator.comparingInt(method -> method.getAnnotation(GetterOrder.class).value()));

        // Print the getter methods in the order they are declared
        for (Method method : getterMethods) {
            System.out.println(method.getName());
        }
    }

    @Test
    public void tetMethodNameAsOrder() {
        Class<MyClass> clazz = MyClass.class;

        List<Method> getterMethods = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (isGetterMethod(method)) {
                getterMethods.add(method);
            }
        }

        // Sort the getter methods by their declaration order
        getterMethods.sort(Comparator.comparingInt(Method::getModifiers));

        // Print the getter methods in the order they are declared
        for (Method method : getterMethods) {
            System.out.println(method.getName());
        }
    }

    @Test
    public void testOrderedGetter() {
        Class<MyClass> clazz = MyClass.class;

        List<Method> getterMethods = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(OrderedGetter.class)) {
                String fieldName = field.getName();
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method method = clazz.getMethod(methodName);
                    getterMethods.add(method);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        getterMethods.sort(Comparator.comparingInt(method -> {
            OrderedGetter orderedGetter = method.getAnnotation(OrderedGetter.class);
            return orderedGetter != null ? orderedGetter.value() : 0;
        }));

        for (Method method : getterMethods) {
            System.out.println(method.getName());
        }
    }

    private static boolean isGetterMethod(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterCount() > 0) {
            return false;
        }
        if (void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }
}

