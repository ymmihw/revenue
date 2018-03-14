package com.whimmy.revenue.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class DefaultExcelReader implements ExcelReader {

  @Override
  public List<String[]> read(Workbook workbook, int sheet, int fromRow, int columnCount,
      int lastRowNum) {
    Sheet xssfSheet = workbook.getSheetAt(sheet);

    List<String[]> list = new ArrayList<>();
    String[] ss = null;
    for (int row = fromRow; row <= lastRowNum; row++) {
      Row xssfRow = xssfSheet.getRow(row);
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
  public List<String[]> read(Workbook workbook, int fromRow, int columnCount, int lastRowNum) {
    return read(workbook, 0, fromRow, columnCount, lastRowNum);
  }

  private String extractCellValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    DataFormatter formatter = new DataFormatter();
    String value = formatter.formatCellValue(cell);
    return value;
  }

  private boolean isNullRow(Row xssfRow) {
    if (xssfRow == null || xssfRow.getPhysicalNumberOfCells() <= 0) {
      return true;
    }
    return false;
  }
}
