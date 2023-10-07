package org.gobeshona.ExcelDto;


import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class DtoToExcelConverter {

    public <T> void dtoToExcel(List<T> dtoList, T dtoObject, String workSheetName, String filePath) throws Exception {
        List<ObjectMetaData> metaDataList = new MetadataCreator().createMetadata(dtoObject);

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(workSheetName);

        // Setting header.
        Row rowHeader = sheet.createRow(0);
        for (int h = 0; h < metaDataList.size(); h++) {
            rowHeader.createCell(h).setCellValue(metaDataList.get(h).getFieldName());
        }

        // Setting row data
        for (int rowCount = 0; rowCount < dtoList.size(); rowCount++) {
            Row row = sheet.createRow(rowCount + 1);
            T dto = dtoList.get(rowCount);
            for (int cellCount = 0; cellCount < metaDataList.size(); cellCount++) {
                Cell cell = row.createCell(cellCount);
                Object data = invokeMethod(dto, metaDataList.get(cellCount).getGetterMethodName());

                // Setting cell type
                String dataType = metaDataList.get(cellCount).getDataType();
                if (dataType.equals(DataType.stringPrim) || dataType.equals(DataType.stringObject)) {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(((String) Optional.ofNullable(data).orElse("-").toString()));
                } else if (checkNumericType(dataType)) {
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(((Number) Optional.ofNullable(data).orElse(0)).intValue());
                } else if (checkBooleanType(dataType)) {
                    cell.setCellType(CellType.BOOLEAN);
                    cell.setCellValue(((Boolean) Optional.ofNullable(data).orElse(false)).booleanValue());
                } else {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(((String) Optional.ofNullable(data).orElse("-").toString()));
                }
            }
        }

        // Create excel sheet
        String fileLocation = new File(filePath) + "/" + sheet.getSheetName() + ".xlsx";
        FileOutputStream out = new FileOutputStream(fileLocation);
        workbook.write(out);
        out.close();
    }

    public static Object invokeMethod(Object object, String methodName) throws Exception {
        Class<?> objectClass = object.getClass();
        Object data = null;
        try {
            Method method = objectClass.getMethod(methodName);
            data = method.invoke(object);
        } catch (Exception e) {
//            System.out.println("methodName = " + methodName);
//            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return data;
    }

    public boolean checkBooleanType(String dataType) {
        return dataType.equals(DataType.booleanPrim) || dataType.equals(DataType.booleanObject);
    }

    public boolean checkNumericType(String dataType) {
        if (dataType.equals(DataType.shortPrim) || dataType.equals(DataType.SHORT_NUMBER)) {
            return true;
        } else if (dataType.equals(DataType.INT) || dataType.equals(DataType.INTEGER_NUMBER) || dataType.equals(DataType.ATOMICINTEGER_NUMBER)) {
            return true;
        } else if (dataType.equals(DataType.longPrim) || dataType.equals(DataType.LONG_NUMBER) || dataType.equals(DataType.ATOMICLONG_NUMBER)) {
            return true;
        } else if (dataType.equals(DataType.floatPrim) || dataType.equals(DataType.floatObject)) {
            return true;
        } else if (dataType.equals(DataType.doublePrim) || dataType.equals(DataType.doubleObject)) {
            return true;
        } else if (dataType.equals(DataType.BIGDECIMAL_NUMBER)) {
            return true;
        } else {
            return false;
        }
    }

}
