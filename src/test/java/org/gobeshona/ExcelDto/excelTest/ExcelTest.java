package org.gobeshona.ExcelDto.excelTest;


import com.salesmonitoringsystem.lankabangla.salesmonitoring.Util.DataType;
import com.salesmonitoringsystem.lankabangla.salesmonitoring.settings.target.TargetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

//@SpringBootTest
//@RunWith(SpringRunner.class)
//@ActiveProfiles("abedeen")
@Slf4j
public class ExcelTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TargetService targetService;

    @Test
    public void testGetTargetByRoNameAndProductType() {
        System.out.println(targetService.getTargetByRoNameAndProductType("1171101464", "96"));
    }

    //    @Test
    public void fileCreateTest() {
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //    @Test
    public void fileCreateDeleteTest() {
        File myObj = new File("filename.txt");
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                for (int x = 0; x < 100; x++) {
                    System.out.println("wait : " + x);
                    Thread.sleep(1000);
                }
            } else {
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

//        File myObj = new File("filename.txt");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    @Test
    public void testWriteToExcel() throws Exception {
        String header[] = {"ROLL", "STUDENT_ID", "NAME"};
        List<StudentDTO> studentDTOList = StudentDTO.generateStudentList(100);
        String workSheetName = "student_report";
        writeExcel(workSheetName, studentDTOList, header);
    }

    @Test
    public void testTypeDetermine() {
        StudentDTO studentDTO = new StudentDTO(1, "John", 101);
        int cellCount = 1; // Example cell count
        Type returnType = studentDTO.getInvokedReturnType(cellCount);

        List<Class<?>> numberTypes = Arrays.asList(
                byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class,
                Number.class, BigInteger.class,
                BigDecimal.class
        );

        if (numberTypes.contains(returnType)) {
            System.out.println("Return type is Number");
        } else if (returnType == String.class) {
            System.out.println("Return type is String");
        } else {
            System.out.println("Return type is " + returnType.getTypeName());
        }
    }

    @Test
    public void testGetVarriableNameforHeader() {
//        getGetterMethodNames(new StudentDTO()).forEach(s -> System.out.println("s = " + s));
        String[] header = getGetterMethodNames(new StudentDTO()).toArray(new String[0]);
        for (String h : header) {
            System.out.println("h = " + h);
        }
    }

    /**
     * it returen a object private field value by its name.
     * here head means column name of the XL file.
     */
    @Test
    public void testGetValueByName() {
        StudentDTO studentDTO = new StudentDTO(1, "John", 101);
        String head = "studentid"; // Example head

        String[] header = getGetterMethodNames(new StudentDTO()).toArray(new String[0]);

        for (int i = 0; i < header.length; i++) {
//            Object value = studentDTO.invokeGetterMethod(header[i]);
            Object value = invokeGetterMethod(studentDTO, header[i]);
            System.out.println(value);
        }

    }

    //    start : finel code area
    @Test
    public void testFinalExcelFileCreator() throws Exception {
        List<StudentDTO> dtoList = StudentDTO.generateStudentList(100);
//        List<BookAuthor> dtoList = generateBookAuthors();
        String workSheetName = "BookAuthor_report";
        String filePath = "src/main/resources/generatedExcel";
//        finalDtoToExcel(studentDTOList, new StudentDTO(), workSheetName, filePath);
//        finalDtoToExcelGeneric(dtoList, new BookAuthor(), workSheetName, filePath);
        finalDtoToExcelGenericClass(dtoList, StudentDTO.class, workSheetName, filePath);
//        finalDtoToExcelGeneric(dtoList, workSheetName, filePath);
    }

    public void finalDtoToExcel(List<StudentDTO> dtoList, StudentDTO dtoObject, String workSheetName, String filePath) throws Exception {
        List<ObjectMetaData> metaDataList = new MetadataCreator().createMetadata(dtoObject);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(workSheetName);

        //seeting header.
        Row rowHeader = sheet.createRow(0);
        for (int h = 0; h < metaDataList.size(); h++) {
            rowHeader.createCell(h).setCellValue(metaDataList.get(h).getFieldName());
        }

        //setting row data
        for (int rowCount = 0; rowCount < dtoList.size(); rowCount++) {
            Row row = sheet.createRow(rowCount + 1);
            Object dto = dtoList.get(rowCount);
            for (int cellCount = 0; cellCount < metaDataList.size(); cellCount++) {
                Cell cell = row.createCell(cellCount);
                Object data = invokeMethod(dto, metaDataList.get(cellCount).getGetterMethodName());

                // setting cell type
                String dataType = metaDataList.get(cellCount).getDataType();
                if (dataType.equals(DataType.stringPrim) || dataType.equals(DataType.stringObject)) {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(((String) Optional.ofNullable(data).orElse("-").toString()));
                } else if (checkNumericType(dataType)) {
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(((Number) Optional.ofNullable(data).orElse(0)).intValue());
                } else if (checkBooleanType(dataType)) {
                    cell.setCellType(CellType.BOOLEAN);
                    cell.setCellValue(((Boolean) Optional.ofNullable(data).orElse(0)).booleanValue());
                } else {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(((String) Optional.ofNullable(data).orElse("-").toString()));
                }
            }
        }

        //create excel sheet
        String fileLocation = new File(filePath) + "/" + sheet.getSheetName() + ".xlsx";
        FileOutputStream out = new FileOutputStream(fileLocation);
        workbook.write(out);

        out.close();

    }

    public <T> void finalDtoToExcelGeneric(List<T> dtoList, T dtoObject, String workSheetName, String filePath) throws Exception {
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

    /*
    * same as other class just the difference is that is here, dtoObject replaced by T class.
    *this is not working well, as the previously tired solution, if class got from 1st list object,
    * extra annotation are being added with it.
    * */
    public <T> void finalDtoToExcelGenericClass(List<T> dtoList, Class<T> dtoObject, String workSheetName, String filePath) throws Exception {
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

    /*
     * this version, in the field Name, getting all the fields a class can have, too many fields which are system genererated.
     * dont know how to get rid of this problem.
     *
     * */
    public <T> void finalDtoToExcelGeneric(List<T> dtoList, String workSheetName, String filePath) throws Exception {
        List<ObjectMetaData> metaDataList = new MetadataCreator().createMetadata(dtoList.get(0).getClass());

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
            System.out.println("methodName = " + methodName);
            System.out.println("e.getMessage() = " + e.getMessage());
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

    //    end : finel code area

    public List<BookAuthor> generateBookAuthors() {
        List<BookAuthor> bookAuthors = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 1000; i++) {
            long bookId = i;
            String bookName = "Book Name " + bookId;
            int authorRandom = random.nextInt(5000) + 1;
            long authorId = authorRandom;
            String authorName = "Author Name " + authorRandom;
            long authorCode = authorRandom;
            String bookCode = null;
            if ((i % (random.nextInt(10) + 1) == 0)) {
                bookCode = "BC" + Long.toString(bookId);
                System.out.println("bookCode = " + bookCode);
            }

            BookAuthor bookAuthor = new BookAuthor(bookId, bookName, bookCode, authorId, authorName, authorCode);
            bookAuthors.add(bookAuthor);
        }

        return bookAuthors;
    }


    public void writeExcel(String workSheetName, List<StudentDTO> dtoList, String header[]) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(workSheetName);

        //seeting header.
        Cell cell0 = null;
        Row rowHeader = sheet.createRow(0);
        for (int h = 0; h < header.length; h++) {
            cell0 = rowHeader.createCell(h);
            cell0.setCellValue(header[h]);
        }

        for (int r = 0; r < dtoList.size(); r++) {
            Row row = sheet.createRow(r + 1);
            StudentDTO studentDTO = dtoList.get(r);
//            System.out.println("studentDTO = " + studentDTO.toString());
            for (int cellCount = 0; cellCount < header.length; cellCount++) {
                Cell cell = row.createCell(cellCount);
                String data = studentDTO.invokeGetterMethod(cellCount).toString();
                Type returnType = studentDTO.getInvokedReturnType(cellCount);
                if (returnType == int.class || returnType == Integer.class) {
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(Integer.valueOf(data));
                } else {
                    cell.setCellValue((data));
                }
            }
        }

        //String fileLocation = new File("src\\main\\resources\\generatedExcel").getAbsolutePath() + "\\" + sheet.getSheetName()+".xlsx";
//        String fileLocation = new File(excelServerPath).getAbsolutePath() + "/" + sheet.getSheetName() + ".xlsx";
        String fileLocation = new File("src/main/resources/generatedExcel") + "/" + sheet.getSheetName() + ".xlsx";
        FileOutputStream out = new FileOutputStream(fileLocation);
        workbook.write(out);

        out.close();
    }

    public Object invokeGetterMethod(StudentDTO studentDTO, String head) {
        Method[] methods = studentDTO.getClass().getMethods();
        System.out.println("studentDTO.getClass().getMethods() = " + studentDTO.getClass().getSimpleName());
        for (Method method : methods) {
            if (isGetterMethod(method)) {
                String methodName = method.getName();
                String propertyName = methodName.substring(3).toLowerCase();
                if (propertyName.equals(head.toLowerCase())) {
                    try {
                        return method.invoke(studentDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }


    public List<String> getGetterMethodNames(StudentDTO studentDTO) {
        List<String> getterMethodNames = new ArrayList<>();

        Method[] methods = studentDTO.getClass().getMethods();
        for (Method method : methods) {
            if (isGetterMethod(method)) {
                String methodName = method.getName();
                String propertyName = methodName.substring(3); // Remove "get" prefix
                String formattedName = propertyName.toUpperCase();
                getterMethodNames.add(formattedName);
            }
        }
        return getterMethodNames;
    }

    private boolean isGetterMethod(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get") && !methodName.equals("getClass")
                && method.getParameterCount() == 0 && !void.class.equals(method.getReturnType())) {
            return true;
        }
        return false;
    }


}
