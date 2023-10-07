package org.gobeshona.ExcelDto;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * have to add method :
 * column name and order checker.
 */
@AllArgsConstructor
@Slf4j
public class ExcelToDtoConverter {
    private static final Logger logger = LoggerFactory.getLogger(ExcelToDtoConverter.class);

    public <T> List<T> excelToDtoListGeneric(InputStream inputStream, T dto) throws Exception {
        List<T> dtoList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int MAX_ROW = sheet.getLastRowNum();
            int MAX_COLUMN = sheet.getRow(0).getLastCellNum();
            List<ObjectMetaData> metadataList = new MetadataCreator().createMetadata(dto);

            if (metadataList.size() != MAX_COLUMN) {
                System.out.println("DTO Fields = " + metadataList.size());
                System.out.println("COLUMN IN EXCEL = " + MAX_COLUMN);
                Thread.sleep(100);
                throw new Exception("DTO class and Excel file does have same number of columns!");
            } else {
                for (int rowCount = 1; rowCount < MAX_ROW; rowCount++) {
                    XSSFRow row = sheet.getRow(rowCount);
                    Object dtoNew = dto.getClass().getDeclaredConstructor().newInstance();
                    for (int cellCount = 0; cellCount < MAX_COLUMN; cellCount++) {
                        String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                        Cell cell = row.getCell(cellCount);
                        CellType cellType = null;

                        try {
                            cellType = cell.getCellType();
                        } catch (NullPointerException e) {
                            logger.error("Cell Type Null: Row {}, Cell {}", rowCount, cellCount);
                        } catch (Exception e) {
                            logger.error("An error occurred");
                            // Log the stack trace for all other exceptions
                            logger.error(e.toString(), e);
                        }

                        String dtoFieldType = metadataList.get(cellCount).getDataType();
                        if (cellType != null) {
                            if (cellType.name().equals("NUMERIC")) {
//                                String dtoFieldType = metadataList.get(cellCount).getDataType();
                                Object objectNumber = cell.getNumericCellValue();

                                /**
                                 * "MethodInvoker.invokeMethod" is replaced by a method within the clsas.
                                 * invokeSetterMethod
                                 */

                                if (dtoFieldType.equals(DataType.shortPrim) || dtoFieldType.equals(DataType.SHORT_NUMBER)) {
                                    Short value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).shortValue();
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else if (dtoFieldType.equals(DataType.INT) || dtoFieldType.equals(DataType.INTEGER_NUMBER) || dtoFieldType.equals(DataType.ATOMICINTEGER_NUMBER)) {
                                    Integer value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).intValue();
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else if (dtoFieldType.equals(DataType.longPrim) || dtoFieldType.equals(DataType.LONG_NUMBER)) {
                                    Long value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue();
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else if (dtoFieldType.equals(DataType.floatPrim) || dtoFieldType.equals(DataType.floatObject)) {
                                    Float value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).floatValue();
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else if (dtoFieldType.equals(DataType.doublePrim) || dtoFieldType.equals(DataType.doubleObject)) {
                                    Double value1 = ((Number) Optional.ofNullable(cell.getNumericCellValue()).orElse(0.0)).doubleValue();
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else if (dtoFieldType.equals(DataType.BIGINTEGER_NUMBER)) {
                                    BigInteger value1 = BigInteger.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue());
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else if (dtoFieldType.equals(DataType.BIGDECIMAL_NUMBER)) {
                                    BigDecimal value1 = BigDecimal.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).doubleValue());
                                    invokeSetterMethod(dtoNew, methodName, value1);
                                } else {
                                    logger.error("Field Type MisMatch. R{}:C{} Cell Type : {}, DTO Field Type: {}", rowCount, cellCount, cellType.name(), dtoFieldType);
                                }
                            } else if (cellType.name().equals("STRING")) {
                                if (dtoFieldType.equals(DataType.stringObject)) {
                                    invokeSetterMethod(dtoNew, methodName, cell.getStringCellValue());
                                } else {
                                    logger.error("Field Type MisMatch. R{}:C{} Cell Type : {}, DTO Field Type: {}", rowCount, cellCount, cellType.name(), dtoFieldType);
                                }
                            } else if (cellType.name().equals("BOOLEAN")) {
                                if (dtoFieldType.equals(DataType.booleanObject)) {
                                    invokeSetterMethod(dtoNew, methodName, cell.getStringCellValue());
                                } else {
                                    logger.error("Field Type MisMatch. R{}:C{} Cell Type : {}, DTO Field Type: {}", rowCount, cellCount, cellType.name(), dtoFieldType);
                                }
                            }
                        }
                    }
                    dtoList.add((T) dtoNew);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    private void invokeSetterMethod(Object object, String methodName, Object parameter) throws Exception {
        Class<?> objectClass = object.getClass();
        Method method = objectClass.getMethod(methodName, parameter.getClass());
        method.invoke(object, parameter);
    }
}
