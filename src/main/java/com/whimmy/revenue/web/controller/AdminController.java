package com.whimmy.revenue.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.whimmy.revenue.util.ExcelReader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Controller
public class AdminController {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        List<String[]> datas = null;
        try {
          datas = extract(files);
        } catch (IOException e) {
          logger.error("", e);
          return;
        }

        System.out.println(datas.size());

        // FileOutputStream out = null;
        //
        // try {
        // List<String[]> datas = excelReader.read(inputWorkbook, 0, 10);
        // XSSFWorkbook outputWorkbook = doImport(datas, uploadModel.isSaveOrUpdate(), token);
        // out = new FileOutputStream(outputFile);
        // outputWorkbook.write(out);
        // } catch (IOException e) {
        // logger.error("", e);
        // } finally {
        // if (out != null) {
        // try {
        // out.close();
        // } catch (IOException ignore) {
        // logger.error("", ignore);
        // }
        // }
        //
        // if (inputWorkbook != null) {
        // try {
        // inputWorkbook.close();
        // } catch (IOException ignore) {
        // logger.error("", ignore);
        // }
        // }
        //
        // }
        // progress.remove(token);
      }

      private List<String[]> extract(List<File> files) throws IOException, FileNotFoundException {
        List<String[]> datas = new ArrayList<>();

        for (File file : files) {
          try (XSSFWorkbook inputWorkbook = new XSSFWorkbook(new FileInputStream(file))) {
            datas.addAll(excelReader.read(inputWorkbook, 0, 10));
          }
        }
        return datas;
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
