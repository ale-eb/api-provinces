package com.pantera.apiprovinces.repository;

import com.pantera.apiprovinces.domain.Province;
import com.pantera.apiprovinces.vo.CentroideVo;

import java.util.List;

public interface ProvinceRepository {

  List<Province> getCoordinates(String provinceName);
}
