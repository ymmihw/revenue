package com.whimmy.revenue.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.whimmy.revenue.web.model.Criteria;
import com.whimmy.revenue.web.model.Data;
import com.whimmy.revenue.web.model.DataTableObject;

@Controller
public class HomeController {
  @RequestMapping(value = {"/", "/home.html", "/home"})
  public String home(HttpServletRequest request) {
    return "/home";
  }

  @PostMapping(value = "/doSearch", produces = "application/json;charset=UTF-8")
  public @ResponseBody DataTableObject<Data> doSearch(final @RequestBody Criteria criteria) {

    DataTableObject<Data> dataTableObject = new DataTableObject<>();
    dataTableObject.setRecordsFiltered(10);
    dataTableObject.setRecordsFiltered(10);
    List<Data> datas = new ArrayList<>();
    Data e = new Data();
    e.setIdCardNo("123");
    datas.add(e);
    dataTableObject.setData(datas);
    return dataTableObject;
  }
}
