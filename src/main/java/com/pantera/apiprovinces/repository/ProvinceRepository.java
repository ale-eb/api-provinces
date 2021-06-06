package com.pantera.apiprovinces.repository;

import com.pantera.apiprovinces.domain.Province;

import java.util.List;

public interface ProvinceRepository {

  List<Province> getCoordinates(String provinceName);
}
