package com.pantera.apiprovinces.repository.impl;

import com.pantera.apiprovinces.repository.ProvinceRepository;
import com.pantera.apiprovinces.vo.CentroideVo;
import com.pantera.apiprovinces.vo.ProvinceVo;
import com.pantera.apiprovinces.vo.ProvincesResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProvinceRepositoryImpl implements ProvinceRepository {

  @Value("${gob.api.url.provinces.get}")
  private String getProvincesUrl;

  private final RestTemplate restTemplate;

  @Override
  public CentroideVo getCoordinates(String provinceName) {
    URI uri = createGetProvincesUri();
    RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

    ResponseEntity<ProvincesResponseVo> responseEntity = restTemplate.exchange(requestEntity, ProvincesResponseVo.class);
    List<ProvinceVo> provinces = responseEntity.getBody().getProvincias();
    Optional<ProvinceVo> optionalProvinceVo = provinces.stream()
            .filter(provinceVo -> provinceVo.getNombre().equals(provinceName)).findFirst();

    return optionalProvinceVo.get().getCentroide();
  }

  private URI createGetProvincesUri() {
    return UriComponentsBuilder
            .fromHttpUrl(getProvincesUrl)
            .build()
            .toUri();
  }
}
