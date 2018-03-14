package com.whimmy.revenue.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.whimmy.revenue.db.entity.TradeEntity;
import com.whimmy.revenue.db.service.ITradeService;
import com.whimmy.revenue.util.ExcelReader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Controller
public class AdminController {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private ITradeService tradeService;
  @Autowired
  private ExcelReader excelReader;

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
          try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            int lastRowNum = workbook.getSheetAt(0).getLastRowNum();

            int step = 100;
            for (int i = 0; i < lastRowNum; i += step) {
              List<String[]> read = excelReader.read(workbook, i, 6, i + step - 1);

              List<TradeEntity> a = read.stream().filter(e -> StringUtils.isNumeric(e[0]))
                  .map(f -> new TradeEntity(f)).collect(Collectors.toList());
              tradeService.save(a);
              logger.debug("save {}/{}", i, lastRowNum);
            }
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
