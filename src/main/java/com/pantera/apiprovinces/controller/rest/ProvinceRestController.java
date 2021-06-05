package com.pantera.apiprovinces.controller.rest;

import com.pantera.apiprovinces.domain.Province;
import com.pantera.apiprovinces.service.ProvinceService;
import com.pantera.apiprovinces.vo.CentroideVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/provinces")
public class ProvinceRestController {

  private final ProvinceService provinceService;

  @GetMapping("/coordinates")
  List<Province> getCoordinates(@RequestParam String provinceName) {
    log.info("Find coordinates to: {}", provinceName);
    return provinceService.getCoordinates(provinceName);
  }
}
