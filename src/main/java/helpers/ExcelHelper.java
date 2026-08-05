package helpers;

import exceptions.InvalidPathForExcelException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ExcelHelper {
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook workbook;
    private Sheet sheet;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public void setExcelFile(String ExcelPath, String SheetName) {
        try {
            File f = new File(ExcelPath);

            if (!f.exists()) {
                f.createNewFile();
                System.out.println("File doesn't exist, so created!");
            }

            fis = new FileInputStream(ExcelPath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(SheetName);
            //sh = wb.getSheetAt(0); //0 - index of 1st sheet
            if (sheet == null) {
                sheet = workbook.createSheet(SheetName);
            }

            this.excelFilePath = ExcelPath;

            //adding all the column header names to the map 'columns'
            sheet.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getCellData(int rownum, int colnum) {
        try {
            cell = sheet.getRow(rownum).getCell(colnum);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public String getCellData(String columnName, int rownum) {
        return getCellData(rownum, columns.get(columnName));
    }

    public int getLastRowNum() {
        return sheet.getLastRowNum();
    }

    public int getPhysicalNumberOfRows() {
        return sheet.getPhysicalNumberOfRows();
    }

    public int getColumns() {
        try {
            row = sheet.getRow(0);
            return row.getLastCellNum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    // Write data to excel sheet
    //set by column index
    public void setCellData(String text, int rowNumber, int colNumber) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(colNumber);

            if (cell == null) {
                cell = row.createCell(colNumber);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed") {
                style.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
            } else if (text == "fail" || text == "failed") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            } else {
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            }

            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //set by column name
    public void setCellData(String text, int rowNumber, String columnName) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(columns.get(columnName));

            if (cell == null) {
                cell = row.createCell(columns.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed") {
                style.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
            } else if (text == "fail" || text == "failed") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            } else {
                style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            }

            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private String getCellStringValue(Cell sourceCell) {
        if (sourceCell == null || sourceCell.getCellType() == CellType.BLANK) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(sourceCell).trim();
    }


    public static Object[][] getExcelData(String fileName, String sheetName) {
        Object[][] data = null;
        Workbook workbook = null;
        try {
            // Validate file exists
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                throw new InvalidPathForExcelException("Excel file not found: " + fileName);
            }

            // load the file
            FileInputStream fis = new FileInputStream(fileName);
            String fileExtensionName = fileName.substring(fileName.indexOf("."));

            // load the workbook
            //workbook = new HSSFWorkbook(fis); //read file excel .xls
            workbook = new XSSFWorkbook(fis); //read file excel .xlsx

            // load the sheet
            Sheet sheet = workbook.getSheet(sheetName);

            // load the row
            Row row = sheet.getRow(0);

            int noOfRows = sheet.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();

            System.out.println(noOfRows + " - " + noOfCols);

            Cell cell;
            data = new Object[noOfRows - 1][noOfCols];

            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols; j++) {
                    row = sheet.getRow(i);
                    cell = row.getCell(j);

                    switch (cell.getCellType()) {
                        case STRING:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i - 1][j] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BLANK:
                            data[i - 1][j] = "";
                            break;
                        default:
                            data[i - 1][j] = null;
                            break;
                    }
                }
            }
        } catch (InvalidPathForExcelException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidPathForExcelException("Failed to read Excel file: " + fileName, e);
        }
        return data;
    }

    public Object[][] getExcelDataHashTable(String excelPath, String sheetName, int startRow, int endRow) {
        System.out.println("Excel Path: " + excelPath);
        Object[][] data = null;

        try {
            File f = new File(excelPath);
            if (!f.exists()) {
                throw new InvalidPathForExcelException("Excel file not found: " + excelPath);
            }

            fis = new FileInputStream(excelPath);

            workbook = new XSSFWorkbook(fis);

            sheet = workbook.getSheet(sheetName);

            int rows = getLastRowNum();
            int columns = getColumns();

            System.out.println("Row: " + rows + " - Column: " + columns);
            System.out.println("StartRow: " + startRow + " - EndRow: " + endRow);

            data = new Object[(endRow - startRow) + 1][1];
            Hashtable<String, String> table = null;
            for (int rowNums = startRow; rowNums <= endRow; rowNums++) {
                table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));
                }
                data[rowNums - startRow][0] = table;
            }

        } catch (InvalidPathForExcelException e) {
            throw e;
        } catch (IOException e) {
            throw new InvalidPathForExcelException("Failed to read Excel file: " + excelPath, e);
        }

        return data;
    }

    // INSERT NEW REGISTERED USER TO DataUser.xlsx
    public void appendRegisteredUserCredentials(String excelPath, String sheetName, String email, String password) {
        Workbook writableWorkbook = null;

        try {
            File excelFile = new File(excelPath);

            if (excelFile.exists() && excelFile.length() > 0) {
                try (FileInputStream inputStream = new FileInputStream(excelFile)) {
                    writableWorkbook = WorkbookFactory.create(inputStream);
                }
            } else {
                writableWorkbook = new XSSFWorkbook();
            }

            Sheet targetSheet = writableWorkbook.getSheet(sheetName);
            if (targetSheet == null) {
                targetSheet = writableWorkbook.createSheet(sheetName);
            }

            ensureRegisteredUserHeader(targetSheet);

            int nextRowIndex = findNextDataRowIndex(targetSheet);
            Row dataRow = targetSheet.getRow(nextRowIndex);
            if (dataRow == null) {
                dataRow = targetSheet.createRow(nextRowIndex);
            }

            dataRow.createCell(0).setCellValue(email);
            dataRow.createCell(1).setCellValue(password);

            try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                writableWorkbook.write(outputStream);
            }
        } catch (Exception exception) {
            throw new RuntimeException("Failed to append registered user credentials to Excel.", exception);
        } finally {
            if (writableWorkbook != null) {
                try {
                    writableWorkbook.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    // HELPER METHOD: Ensure EMAIL and PASSWORD headers exist in the target sheet
    private void ensureRegisteredUserHeader(Sheet targetSheet) {
        Row headerRow = targetSheet.getRow(0);
        if (headerRow == null) {
            headerRow = targetSheet.createRow(0);
        }

        Cell emailHeaderCell = headerRow.getCell(0);
        if (emailHeaderCell == null || emailHeaderCell.getCellType() == CellType.BLANK) {
            headerRow.createCell(0).setCellValue("EMAIL");
        }

        Cell passwordHeaderCell = headerRow.getCell(1);
        if (passwordHeaderCell == null || passwordHeaderCell.getCellType() == CellType.BLANK) {
            headerRow.createCell(1).setCellValue("PASSWORD");
        }
    }

    private int findNextDataRowIndex(Sheet targetSheet) {
        int lastRowIndex = targetSheet.getLastRowNum();

        if (lastRowIndex < 1) {
            return 1;
        }

        for (int rowIndex = lastRowIndex; rowIndex >= 1; rowIndex--) {
            Row row = targetSheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            String emailValue = getCellStringValue(row.getCell(0));
            String passwordValue = getCellStringValue(row.getCell(1));
            if (!emailValue.isEmpty() || !passwordValue.isEmpty()) {
                return rowIndex + 1;
            }
        }

        return 1;
    }

}
