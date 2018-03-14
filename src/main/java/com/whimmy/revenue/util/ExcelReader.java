package com.whimmy.revenue.util;

import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelReader {
    List<String[]> read(Workbook workbook, int sheet, int fromRow, int columnCount, int lastRowNum);

    List<String[]> read(Workbook workbook, int fromRow, int columnCount, int lastRowNum);
}
