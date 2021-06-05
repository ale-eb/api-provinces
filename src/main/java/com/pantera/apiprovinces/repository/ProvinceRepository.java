package com.pantera.apiprovinces.repository;

import com.pantera.apiprovinces.vo.CentroideVo;

import java.util.List;

public interface ProvinceRepository {

  List<CentroideVo> getCoordinates(String provinceName);
}
