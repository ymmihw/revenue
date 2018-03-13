package com.whimmy.revenue.util;

import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ExcelReader {
  List<String[]> read(XSSFWorkbook workbook, int sheet, int fromRow, int columnCount);

  List<String[]> read(XSSFWorkbook workbook, int fromRow, int columnCount);
}
