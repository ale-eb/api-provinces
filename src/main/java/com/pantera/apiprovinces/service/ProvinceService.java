package com.pantera.apiprovinces.service;

import com.pantera.apiprovinces.vo.CentroideVo;

public interface ProvinceService {

  CentroideVo getCoordinates(String provinceName);
}
