package com.pantera.apiprovinces.service.impl;

import com.pantera.apiprovinces.repository.ProvinceRepository;
import com.pantera.apiprovinces.service.ProvinceService;
import com.pantera.apiprovinces.vo.CentroideVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

  private final ProvinceRepository provinceRepository;

  @Override
  public CentroideVo getCoordinates(String provinceName) {
    return provinceRepository.getCoordinates(provinceName);
  }
}
