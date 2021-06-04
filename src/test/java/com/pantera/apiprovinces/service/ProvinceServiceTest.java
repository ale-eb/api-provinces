package com.pantera.apiprovinces.service;

import com.pantera.apiprovinces.domain.Coordinates;
import com.pantera.apiprovinces.vo.CentroideVo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockRestServiceServer
public class ProvinceServiceTest {

  @Value("${gob.api.url.provinces.get}")
  private String getProvinceUrl;

  @Autowired
  private ProvinceService provinceService;

  @Autowired
  private MockRestServiceServer mockRestServiceServer;

  @Test
  void getCoordinates_withExistingProvinceName_returnCoordinates() {
    String responseApi = getFileContent("api-responses/api-provinces-response.json");
    String provinceName = "Buenos Aires";
    ResponseCreator requestResponse = MockRestResponseCreators.withSuccess(responseApi, MediaType.APPLICATION_JSON);
    mockGetProvinceRequest(requestResponse);

    CentroideVo coordinates = provinceService.getCoordinates(provinceName);

    mockRestServiceServer.verify();
    assertThat(coordinates.getLat()).isEqualTo(-36.6769415180527);
    assertThat(coordinates.getLon()).isEqualTo(-60.5588319815719);
  }

  private void mockGetProvinceRequest(ResponseCreator requestResponse) {
    URI provincesUri = UriComponentsBuilder.fromUriString(getProvinceUrl).build().toUri();
    mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(provincesUri))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(requestResponse);
  }

  @SneakyThrows
  private String getFileContent(String filePath) {
    File file = new ClassPathResource(filePath).getFile();
    return FileCopyUtils.copyToString(new FileReader(file));
  }


}
