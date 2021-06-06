package com.pantera.apiprovinces.repository.impl;

import com.pantera.apiprovinces.domain.Province;
import com.pantera.apiprovinces.repository.ProvinceRepository;
import com.pantera.apiprovinces.vo.ProvinceVo;
import com.pantera.apiprovinces.vo.ProvincesResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProvinceRepositoryImpl implements ProvinceRepository {

  @Value("${gob.api.url.provinces.get}")
  private String getProvincesUrl;

  private final RestTemplate restTemplate;

  @Override
  public List<Province> getCoordinates(String provinceName) {
    URI uri = createGetProvincesUri(provinceName);
    RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

    try {
      ResponseEntity<ProvincesResponseVo> responseEntity = restTemplate.exchange(requestEntity, ProvincesResponseVo.class);
      List<ProvinceVo> provincesVo = responseEntity.getBody().getProvincias();
      return transformToProvinces(provincesVo);
    } catch (HttpClientErrorException errorException) {
      throw new IllegalArgumentException("The province name do not have contents.", errorException);
    }

  }

  private List<Province> transformToProvinces(List<ProvinceVo> provincesVo) {
    return provincesVo.stream().map(provinceVo -> {
      Province province = new Province();
      province.setName(provinceVo.getNombre());
      province.setLatitude(provinceVo.getCentroide().getLat());
      province.setLongitude(provinceVo.getCentroide().getLon());
      return province;
    }).collect(Collectors.toList());
  }

  private URI createGetProvincesUri(String provinceName) {
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("nombre", provinceName);
    return UriComponentsBuilder
            .fromHttpUrl(getProvincesUrl)
            .buildAndExpand(uriVariables)
            .toUri();
  }
}
