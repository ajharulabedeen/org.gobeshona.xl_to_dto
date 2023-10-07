package org.gobeshona.ExcelDto.excelTest;

import org.gobeshona.ExcelDto.ExcelToDtoConverter;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


public class ExcelToDtoConverterTest {

    @Test
    public void test() {
        System.out.println("ExcelToDtoConverterTest.test");
    }

    @Test
    public void testFinalGeneric() throws Exception {

        ExcelToDtoConverter converter = new ExcelToDtoConverter();

        InputStream inputStream = new FileInputStream("input.xlsx");
        List<ExcelToDto_DTO> excelToDtoList = converter.excelToDtoList(inputStream, new ExcelToDto_DTO());
        for (ExcelToDto_DTO dto : excelToDtoList) {
            System.out.println("dto = " + dto);
        }
    }
}
