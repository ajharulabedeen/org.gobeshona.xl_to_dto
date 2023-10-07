package org.gobeshona.ExcelDto.excelTest;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gobeshona.ExcelDto.DataType;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ExcelToDto {

    MetadataCreator metadataCreator = new MetadataCreator();

    @Test
    public void testInputStream() throws Exception {
//        InputStream inputStream = getExcelFile("input.xlsx");
        InputStream inputStream = getExcelFile("input.txt");
        System.out.println("inputStream = " + inputStream);
    }

    /**
     * it is not working properly.
     *
     * @throws Exception
     */
    @Test
    public void testExcelToDtoList() throws Exception {
        InputStream inputStream = new FileInputStream("input.xlsx");
        //        InputStream inputStream = getExcelFile("input.xlsx");
        System.out.println("inputStream = " + inputStream);
        List<ExcelToDto_DTO> dtoList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int MAX_ROW = sheet.getLastRowNum();
            int MAX_COLUMN = sheet.getRow(0).getLastCellNum();
            List<ObjectMetaData> metadataList = metadataCreator.createMetadata(new ExcelToDto_DTO());
            for (int rowCount = 0; rowCount < MAX_ROW; rowCount++) {
                XSSFRow row = sheet.getRow(rowCount + 1);
                ExcelToDto_DTO dto = new ExcelToDto_DTO();
                for (int cellCount = 0; cellCount < MAX_COLUMN; cellCount++) {
                    Cell cell = row.getCell(cellCount);
                    CellType cellType = cell.getCellType();

                    if (cellType.name().equals("NUMERIC")) {
                        String dtoFieldType = metadataList.get(cellCount).getDataType();
                        String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                        Object objectNumber = cell.getNumericCellValue();

                        if (dtoFieldType.equals(DataType.shortPrim) || dtoFieldType.equals(DataType.SHORT_NUMBER)) {
                            Short value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).shortValue();
                            MethodInvoker.invokeMethod(dto, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.INT) || dtoFieldType.equals(DataType.INTEGER_NUMBER) || dtoFieldType.equals(DataType.ATOMICINTEGER_NUMBER)) {
                            Integer value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).intValue();
                            MethodInvoker.invokeMethod(dto, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.longPrim) || dtoFieldType.equals(DataType.LONG_NUMBER) || dtoFieldType.equals(DataType.ATOMICLONG_NUMBER)) {
                            Long value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue();
                            MethodInvoker.invokeMethod(dto, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.floatPrim) || dtoFieldType.equals(DataType.floatObject)) {
                            Float value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).floatValue();
                            MethodInvoker.invokeMethod(dto, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.doublePrim) || dtoFieldType.equals(DataType.doubleObject)) {
                            Double value1 = ((Number) Optional.ofNullable(cell.getNumericCellValue()).orElse(0.0)).doubleValue();
                            MethodInvoker.invokeMethod(dto, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.BIGDECIMAL_NUMBER)) {
                            BigDecimal value1 = BigDecimal.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).doubleValue());
                            MethodInvoker.invokeMethod(dto, methodName, value1);
                        }
                    } else if (cellType.name().equals("STRING")) {
                        String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                        MethodInvoker.invokeMethod(dto, methodName, cell.getStringCellValue());
                        dtoList.add(dto);
                    } else if (cellType.name().equals("BOOLEAN")) {

                    }
                    dtoList.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dtoList.forEach(excelToDto_dto -> System.out.println("excelToDto_dto = " + excelToDto_dto));

    }


//    start : dont touch

    /**
     * working---------------------
     * this method was not working properly, abadanondod.
     * but later fixed, the problem was not adding the DTO in the list.
     *
     * @throws Exception
     */
    @Test
    public void testFinal() throws Exception {
        InputStream inputStream = new FileInputStream("input.xlsx");
        List<ExcelToDto_DTO> excelToDtoList = excelToDtoList(inputStream, new ExcelToDto_DTO());
    }

    public List<ExcelToDto_DTO> excelToDtoList(InputStream inputStream, ExcelToDto_DTO dto) throws Exception {
        List<ExcelToDto_DTO> dtoList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int MAX_ROW = sheet.getLastRowNum();
            int MAX_COLUMN = sheet.getRow(0).getLastCellNum();
            List<ObjectMetaData> metadataList = metadataCreator.createMetadata(new ExcelToDto_DTO());

            for (int rowCount = 0; rowCount < MAX_ROW; rowCount++) {
                XSSFRow row = sheet.getRow(rowCount + 1);
                ExcelToDto_DTO dtoNew = new ExcelToDto_DTO();
                for (int cellCount = 0; cellCount < MAX_COLUMN; cellCount++) {
                    String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                    Cell cell = row.getCell(cellCount);
                    CellType cellType = cell.getCellType();
                    if (cellType.name().equals("NUMERIC")) {
                        String dtoFieldType = metadataList.get(cellCount).getDataType();
                        Object objectNumber = cell.getNumericCellValue();

                        if (dtoFieldType.equals(DataType.shortPrim) || dtoFieldType.equals(DataType.SHORT_NUMBER)) {
                            Short value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).shortValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.INT) || dtoFieldType.equals(DataType.INTEGER_NUMBER) || dtoFieldType.equals(DataType.ATOMICINTEGER_NUMBER)) {
                            Integer value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).intValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.longPrim) || dtoFieldType.equals(DataType.LONG_NUMBER)) {
                            Long value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.floatPrim) || dtoFieldType.equals(DataType.floatObject)) {
                            Float value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).floatValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.doublePrim) || dtoFieldType.equals(DataType.doubleObject)) {
                            Double value1 = ((Number) Optional.ofNullable(cell.getNumericCellValue()).orElse(0.0)).doubleValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.BIGINTEGER_NUMBER)) {
                            BigInteger value1 = BigInteger.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue());
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.BIGDECIMAL_NUMBER)) {
                            BigDecimal value1 = BigDecimal.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).doubleValue());
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        }
                    } else if (cellType.name().equals("STRING")) {
                        MethodInvoker.invokeMethod(dtoNew, methodName, cell.getStringCellValue());
                    } else if (cellType.name().equals("BOOLEAN")) {
                        MethodInvoker.invokeMethod(dtoNew, methodName, cell.getStringCellValue());
                    }
                }
                dtoList.add(dtoNew);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        dtoList.forEach(excelToDto_dto -> System.out.println("excelToDto_dto = " + excelToDto_dto));
        return dtoList;
    }
//    end : dont touch


    /**
     * it is also working kept just for testing purpose. d
     *
     * @param inputStream
     * @param dto
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> excelToDtoList(InputStream inputStream, Class<T> dto) throws Exception {
//        Object instance = dto.newInstance();
        List<T> dtoList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int MAX_ROW = sheet.getLastRowNum();
            int MAX_COLUMN = sheet.getRow(0).getLastCellNum();
            List<ObjectMetaData> metadataList = metadataCreator.createMetadata(dto);

            for (int rowCount = 0; rowCount < MAX_ROW; rowCount++) {
                XSSFRow row = sheet.getRow(rowCount + 1);
//                T dtoNew = dto.getClass().getDeclaredConstructor().newInstance();
                Object dtoNew = dto.newInstance();
                for (int cellCount = 0; cellCount < MAX_COLUMN; cellCount++) {
                    String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                    Cell cell = row.getCell(cellCount);
                    CellType cellType = cell.getCellType();
                    if (cellType.equals(CellType.NUMERIC)) {
                        String dtoFieldType = metadataList.get(cellCount).getDataType();
                        Object objectNumber = cell.getNumericCellValue();

                        if (dtoFieldType.equals(DataType.shortPrim) || dtoFieldType.equals(DataType.SHORT_NUMBER)) {
                            Short value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).shortValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.INT) || dtoFieldType.equals(DataType.INTEGER_NUMBER) || dtoFieldType.equals(DataType.ATOMICINTEGER_NUMBER)) {
                            Integer value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).intValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.longPrim) || dtoFieldType.equals(DataType.LONG_NUMBER)) {
                            Long value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.floatPrim) || dtoFieldType.equals(DataType.floatObject)) {
                            Float value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).floatValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.doublePrim) || dtoFieldType.equals(DataType.doubleObject)) {
                            Double value1 = ((Number) Optional.ofNullable(cell.getNumericCellValue()).orElse(0.0)).doubleValue();
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.BIGINTEGER_NUMBER)) {
                            BigInteger value1 = BigInteger.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue());
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        } else if (dtoFieldType.equals(DataType.BIGDECIMAL_NUMBER)) {
                            BigDecimal value1 = BigDecimal.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).doubleValue());
                            MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                        }
                    } else if (cellType.equals(CellType.STRING)) {
                        MethodInvoker.invokeMethod(dtoNew, methodName, cell.getStringCellValue());
                    } else if (cellType.equals(CellType.BOOLEAN)) {
                        MethodInvoker.invokeMethod(dtoNew, methodName, cell.getBooleanCellValue());
                    }
                }
                T dtom = ((T) dtoNew);
//                dtom = ((T) dtoNew);
//                dtoList.add(dtoNew);
                dtoList.add(dtom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }


    @Test
    public void testFinalGeneric() throws Exception {
//        InputStream inputStream = new FileInputStream("input.xlsx");
        InputStream inputStream = new FileInputStream("Legal Database C&C.xlsx");
//        List<ExcelToDto_DTO> excelToDtoList = excelToDtoListGeneric(inputStream, new ExcelToDto_DTO());
//        for (ExcelToDto_DTO dto : excelToDtoList) {
//            System.out.println("dto = " + dto);
//        }
//        List<LegalCaseInfoDTO> excelToDtoList = excelToDtoListGeneric(inputStream, new LegalCaseInfoDTO());
        List<LegalCaseInfoDTO> excelToDtoList = excelToDtoListGenericStringOnly(inputStream, new LegalCaseInfoDTO());
        System.out.println("excelToDtoList.size() = " + excelToDtoList.size());
        Integer count = 0;
        for (LegalCaseInfoDTO dto : excelToDtoList) {
            count++;
//            System.out.println("dto = " + dto);
            if (dto.getTotalSuitValue() == null) {
                count++;
            }
        }
        System.out.println("excelToDtoList = " + excelToDtoList.get(0));
        System.out.println("count = " + count);
    }


    public <T> List<T> excelToDtoListGenericStringOnly(InputStream inputStream, T dto) throws Exception {
        List<T> dtoList = new ArrayList<>();
        int globalCounter = 0;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int MAX_ROW = sheet.getLastRowNum();
            int MAX_COLUMN = sheet.getRow(0).getLastCellNum();
            List<ObjectMetaData> metadataList = metadataCreator.createMetadata(dto);
            System.out.println("MAX_ROW = " + MAX_ROW);
            System.out.println("MAX_COLUMN = " + MAX_COLUMN);
            if (metadataList.size() != MAX_COLUMN) {
                System.out.println("DTO Fields = " + metadataList.size());
                System.out.println("COLUMN IN EXCEL = " + MAX_COLUMN);
//                Thread.sleep(100);
                throw new Exception("DTO class and Excel file does have same number of column!");
            } else {
                for (int rowCount = 0; rowCount < MAX_ROW; rowCount++) {
                    globalCounter++;
                    XSSFRow row = sheet.getRow(rowCount + 1);
                    Object dtoNew = dto.getClass().getDeclaredConstructor().newInstance();
                    for (int cellCount = 0; cellCount < MAX_COLUMN; cellCount++) {
                        String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                        Cell cell = null;
                        try {
                            cell = row.getCell(cellCount);
                            CellType cellType = cell.getCellType();
                        } catch (Exception e) {
//                            System.out.println("e = " + e);
                        }

                        String cellData = Objects.toString(cell, "");
                        MethodInvoker.invokeMethod(dtoNew, methodName, cellData);

//                        if (cellCount == 22) {
//                            if (rowCount + 1 == 1) {
//                                System.out.println("cellType = " + cellType);
//                                System.out.println("((XSSFCell) cell).getRawValue() = " + ((XSSFCell) cell).getRawValue());
//                                System.out.println("cell = " + cell.getStringCellValue());
//                                System.out.println("methodName = " + methodName);
////                                System.out.println("cellType = " + cellType);
//                            }
//                        }

                    }
                    dtoList.add((T) dtoNew);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("globalCounter = " + globalCounter);
        }
        return dtoList;
    }

    public <T> List<T> excelToDtoListGeneric(InputStream inputStream, T dto) throws Exception {
        List<T> dtoList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int MAX_ROW = sheet.getLastRowNum();
            int MAX_COLUMN = sheet.getRow(0).getLastCellNum();
            List<ObjectMetaData> metadataList = metadataCreator.createMetadata(dto);

            if (metadataList.size() != MAX_COLUMN) {
                System.out.println("DTO Fields = " + metadataList.size());
                System.out.println("COLUMN IN EXCEL = " + MAX_COLUMN);
                Thread.sleep(100);
                throw new Exception("DTO class and Excel file does have same number of column!");
            } else {
                for (int rowCount = 0; rowCount < MAX_ROW; rowCount++) {
                    XSSFRow row = sheet.getRow(rowCount + 1);
//                T dtoNew = dto.getClass().newInstance();
//                T dtoNew = dto.getClass().getDeclaredConstructor().newInstance();
                    Object dtoNew = dto.getClass().getDeclaredConstructor().newInstance();
                    for (int cellCount = 0; cellCount < MAX_COLUMN; cellCount++) {
                        String methodName = metadataList.get(cellCount).getGetterMethodName().replace("get", "set");
                        Cell cell = row.getCell(cellCount);
                        CellType cellType = cell.getCellType();
                        if (cellType.name().equals("NUMERIC")) {
                            String dtoFieldType = metadataList.get(cellCount).getDataType();
                            Object objectNumber = cell.getNumericCellValue();

                            if (dtoFieldType.equals(DataType.shortPrim) || dtoFieldType.equals(DataType.SHORT_NUMBER)) {
                                Short value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).shortValue();
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            } else if (dtoFieldType.equals(DataType.INT) || dtoFieldType.equals(DataType.INTEGER_NUMBER) || dtoFieldType.equals(DataType.ATOMICINTEGER_NUMBER)) {
                                Integer value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).intValue();
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            } else if (dtoFieldType.equals(DataType.longPrim) || dtoFieldType.equals(DataType.LONG_NUMBER)) {
                                Long value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue();
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            } else if (dtoFieldType.equals(DataType.floatPrim) || dtoFieldType.equals(DataType.floatObject)) {
                                Float value1 = ((Number) Optional.ofNullable(objectNumber).orElse(0)).floatValue();
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            } else if (dtoFieldType.equals(DataType.doublePrim) || dtoFieldType.equals(DataType.doubleObject)) {
                                Double value1 = ((Number) Optional.ofNullable(cell.getNumericCellValue()).orElse(0.0)).doubleValue();
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            } else if (dtoFieldType.equals(DataType.BIGINTEGER_NUMBER)) {
                                BigInteger value1 = BigInteger.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).longValue());
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            } else if (dtoFieldType.equals(DataType.BIGDECIMAL_NUMBER)) {
                                BigDecimal value1 = BigDecimal.valueOf(((Number) Optional.ofNullable(objectNumber).orElse(0)).doubleValue());
                                MethodInvoker.invokeMethod(dtoNew, methodName, value1);
                            }
                        } else if (cellType.name().equals("STRING")) {
                            MethodInvoker.invokeMethod(dtoNew, methodName, cell.getStringCellValue());
                        } else if (cellType.name().equals("BOOLEAN")) {
                            MethodInvoker.invokeMethod(dtoNew, methodName, cell.getStringCellValue());
                        } else {
                            MethodInvoker.invokeMethod(dtoNew, methodName, "");
                        }

                        if (cellCount == 22) {
                            if (rowCount + 1 == 1) {
                                System.out.println("cellType = " + cellType);
                                System.out.println("((XSSFCell) cell).getRawValue() = " + ((XSSFCell) cell).getRawValue());
                                System.out.println("cell = " + cell.getStringCellValue());
                                System.out.println("methodName = " + methodName);
//                                System.out.println("cellType = " + cellType);
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

    public InputStream getExcelFile(String fileName) {
        System.out.println("ExcelToDto.testInputStream");

        byte[] array = new byte[100];

        InputStream input = null;
        try {
            System.out.println("TRY : FILE READ");
            input = new FileInputStream(fileName);

            System.out.println("input = " + input);

            System.out.println("Available bytes in the file: " + input.available());

            // Read byte from the input stream
            input.read(array);
            System.out.println("Data read from the file: ");

            // Convert byte array into string
            String data = new String(array);
            System.out.println(data);

            // Close the input stream
//            input.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getStackTrace();
        }

        return input;
    }
}
