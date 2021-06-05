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
import java.util.List;
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
  void getCoordinates_withExistingProvinceName_returnCoordinatesList() {
    String responseApi = getFileContent("api-responses/api-provinces-response.json");
    String provinceName = "Tucuman";
    ResponseCreator requestResponse = MockRestResponseCreators.withSuccess(responseApi, MediaType.APPLICATION_JSON);
    mockGetProvinceRequest(provinceName, requestResponse);

    List<CentroideVo> coordinatesList = provinceService.getCoordinates(provinceName);

    mockRestServiceServer.verify();
    assertThat(coordinatesList.get(0).getLat()).isEqualTo(-26.9478001830786);
    assertThat(coordinatesList.get(0).getLon()).isEqualTo(-65.3647579441481);
  }

  private void mockGetProvinceRequest(String provinceName,ResponseCreator requestResponse) {
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("nombre", provinceName);
    URI provincesUri = UriComponentsBuilder.fromUriString(getProvinceUrl).buildAndExpand(uriVariables).toUri();
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
