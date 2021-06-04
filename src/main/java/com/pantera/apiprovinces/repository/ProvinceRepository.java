package com.pantera.apiprovinces.repository;

import com.pantera.apiprovinces.vo.CentroideVo;

public interface ProvinceRepository {

  CentroideVo getCoordinates(String provinceName);
}
