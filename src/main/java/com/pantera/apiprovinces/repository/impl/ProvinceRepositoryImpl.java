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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProvinceRepositoryImpl implements ProvinceRepository {

  @Value("${gob.api.url.provinces.get}")
  private String getProvincesUrl;

  private final RestTemplate restTemplate;

  @Override
  public List<CentroideVo> getCoordinates(String provinceName) {
    URI uri = createGetProvincesUri(provinceName);
    RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

    ResponseEntity<ProvincesResponseVo> responseEntity = restTemplate.exchange(requestEntity, ProvincesResponseVo.class);
    List<ProvinceVo> provinces = responseEntity.getBody().getProvincias();

    return provinces.stream().map(ProvinceVo::getCentroide).collect(Collectors.toList());
  }

  private URI createGetProvincesUri(String provinceName) {
    Map<String, String> uriVaribales = new HashMap<>();
    uriVaribales.put("nombre", provinceName);
    return UriComponentsBuilder
            .fromHttpUrl(getProvincesUrl)
            .buildAndExpand(uriVaribales)
            .toUri();
  }
}
