package com.whimmy.revenue.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.monitorjbl.xlsx.StreamingReader;
import com.whimmy.revenue.db.entity.TradeEntity;
import com.whimmy.revenue.db.service.ITradeService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Controller
public class AdminController {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private ITradeService tradeService;

  @RequestMapping(value = {"/admin", "/admin.html"})
  public String admin(Model model) {
    if (!model.containsAttribute("uploadModel")) {
      model.addAttribute("uploadModel", new UploadModel());
    }
    return "/admin";
  }

  @RequestMapping(value = "/admin/upload")
  public String handleUpload(final @ModelAttribute("uploadModel") UploadModel uploadModel,
      RedirectAttributes redirectAttributes) throws IOException {
    tradeService.deleteAll();

    List<MultipartFile> multipartFiles = uploadModel.getFiles();
    List<File> files = new ArrayList<>();
    for (MultipartFile file : multipartFiles) {
      File inputFile = File.createTempFile("urcc", null);
      file.transferTo(inputFile);
      files.add(inputFile);
    }

    new Thread() {
      @Override
      public void run() {
        try {
          extract(files);
        } catch (IOException | InvalidFormatException e) {
          logger.error("", e);
          return;
        }
      }

      private void extract(List<File> files)
          throws IOException, FileNotFoundException, InvalidFormatException {

        for (File file : files) {
          try (Workbook workbook =
              StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);) {
            Sheet sheetAt = workbook.getSheetAt(0);
            List<TradeEntity> list = new ArrayList<>();
            for (Iterator<Row> it = sheetAt.rowIterator(); it.hasNext();) {
              Row row = it.next();
              String[] strings = new String[6];
              int i = 0;
              for (Cell c : row) {
                strings[i] = c.getStringCellValue();
                i++;
              }
              if (!StringUtils.isNumeric(strings[0])) {
                continue;
              }

              list.add(new TradeEntity(strings));
              if (list.size() >= 1000) {
                logger.debug("save {}", list.size());
                tradeService.save(list);
                list.clear();
              }
            }
            tradeService.save(list);
            logger.debug("save {}", list.size());
            list.clear();
          }
        }
      };
    }.start();

    return "redirect:/admin";
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class UploadModel {
    private List<MultipartFile> files;
  }
}
