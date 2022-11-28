package com.pantera.apiprovinces.vo;

import lombok.Data;

@Data
public class ProvinceVo {

  private String id;
  private String nombre;
  private CentroideVo centroide;
}
