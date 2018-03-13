package com.whimmy.revenue.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.whimmy.revenue.db.entity.TradeEntity;
import com.whimmy.revenue.db.service.ITradeService;
import com.whimmy.revenue.web.model.Criteria;
import com.whimmy.revenue.web.model.DataTableObject;
import com.whimmy.revenue.web.model.TradeDto;

@Controller
public class HomeController {
  @Autowired
  private ITradeService tradeService;

  private final ModelMapper mapper = new ModelMapper();

  @RequestMapping(value = {"/", "/home.html", "/home"})
  public String home(HttpServletRequest request) {
    return "/home";
  }

  @PostMapping(value = "/doSearch", produces = "application/json;charset=UTF-8")
  public @ResponseBody DataTableObject<TradeDto> doSearch(final @RequestBody Criteria criteria) {

    int size = criteria.getLength();
    int pageNo = criteria.getStart() / size;

    Pageable pageable = new PageRequest(pageNo, size);
    Page<TradeEntity> page = tradeService.findByOwnerIdContaining(criteria.getOwnerId(), pageable);


    DataTableObject<TradeDto> dataTableObject = new DataTableObject<>();
    dataTableObject.setRecordsFiltered(page.getTotalElements());
    List<TradeDto> datas = page.getContent().stream().map(e -> mapper.map(e, TradeDto.class))
        .collect(Collectors.toList());
    dataTableObject.setData(datas);

    return dataTableObject;
  }
}
