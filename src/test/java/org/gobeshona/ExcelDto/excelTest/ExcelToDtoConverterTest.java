package org.gobeshona.ExcelDto.excelTest;

import com.salesmonitoringsystem.lankabangla.salesmonitoring.Util.dto_excel_converter.ExcelToDtoConverter;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class ExcelToDtoConverterTest {

    @Test
    public void test() {
        System.out.println("ExcelToDtoConverterTest.test");
    }

    @Test
    public void testFinalGeneric() throws Exception {

        ExcelToDtoConverter converter = new ExcelToDtoConverter();

        InputStream inputStream = new FileInputStream("input.xlsx");
        List<ExcelToDto_DTO> excelToDtoList = converter.excelToDtoListGeneric(inputStream, new ExcelToDto_DTO());
        for (ExcelToDto_DTO dto : excelToDtoList) {
            System.out.println("dto = " + dto);
        }
    }
}
