package com.whimmy.revenue.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {
  @RequestMapping(value = {"/admin", "/admin.html"})
  public String admin(Model model) {
    if (!model.containsAttribute("uploadModel")) {
      model.addAttribute("uploadModel", new UploadModel());
    }
    return "/admin";
  }

  public class UploadModel {
    private MultipartFile file;

    public MultipartFile getFile() {
      return file;
    }

    public void setFile(MultipartFile file) {
      this.file = file;
    }
  }
}
