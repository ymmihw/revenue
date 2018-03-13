package com.whimmy.revenue.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class DefaultExcelReader implements ExcelReader {

  @Override
  public List<String[]> read(XSSFWorkbook workbook, int sheet, int fromRow, int columnCount) {
    XSSFSheet xssfSheet = workbook.getSheetAt(sheet);
    int lastRowNum = xssfSheet.getLastRowNum();

    List<String[]> list = new ArrayList<>();
    String[] ss = null;
    for (int row = fromRow; row <= lastRowNum; row++) {
      XSSFRow xssfRow = xssfSheet.getRow(row);
      if (isNullRow(xssfRow)) {
        continue;
      }
      ss = new String[columnCount];
      Cell cell = null;
      for (int col = 0; col < columnCount; col++) {
        cell = xssfRow.getCell(col);
        ss[col] = extractCellValue(cell);
      }
      list.add(ss);
    }
    return list;
  }

  @Override
  public List<String[]> read(XSSFWorkbook workbook, int fromRow, int columnCount) {
    return read(workbook, 0, fromRow, columnCount);
  }

  private String extractCellValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    DataFormatter formatter = new DataFormatter();
    String value = formatter.formatCellValue(cell);
    return value;
  }

  private boolean isNullRow(XSSFRow xssfRow) {
    if (xssfRow == null || xssfRow.getPhysicalNumberOfCells() <= 0) {
      return true;
    }
    return false;
  }
}
