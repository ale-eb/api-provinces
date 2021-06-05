package com.pantera.apiprovinces.service.impl;

import com.pantera.apiprovinces.domain.Province;
import com.pantera.apiprovinces.repository.ProvinceRepository;
import com.pantera.apiprovinces.service.ProvinceService;
import com.pantera.apiprovinces.vo.CentroideVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

  private final ProvinceRepository provinceRepository;

  @Override
  public List<Province> getCoordinates(String provinceName) {
    Assert.notNull(provinceName, "The province name cannot be null");
    return provinceRepository.getCoordinates(provinceName);
  }
}
