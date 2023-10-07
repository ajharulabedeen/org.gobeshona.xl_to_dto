package org.gobeshona.ExcelDto.excelTest;

import lombok.Data;

@Data
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyClass {
    private String name;
    private String n_ame;
    private double price;
    private Boolean active;
    private int id;
//    @GetterOrder(1)
//    public int getId() {
//        return id;
//    }
//
//    @GetterOrder(2)
//    public String getName() {
//        return name;
//    }
//
//    @GetterOrder(3)
//    public double getPrice() {
//        return price;
//    }
}
