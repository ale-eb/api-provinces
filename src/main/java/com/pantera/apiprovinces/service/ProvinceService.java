package com.pantera.apiprovinces.service;

import com.pantera.apiprovinces.domain.Province;
import com.pantera.apiprovinces.vo.CentroideVo;

import java.util.List;

public interface ProvinceService {

  List<Province> getCoordinates(String provinceName);
}
