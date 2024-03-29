package com.pantera.apiprovinces.service;

import com.pantera.apiprovinces.domain.Province;
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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
  void getCoordinates_withExistingProvinceName_returnProvinces() {
    String responseApi = getFileContent("api-gob-responses/api-gob-response-with-one-province.json");
    String provinceName = "Tucuman";
    ResponseCreator requestResponse = MockRestResponseCreators.withSuccess(responseApi, MediaType.APPLICATION_JSON);
    mockGetProvinceRequest(provinceName, requestResponse);

    List<Province> provinces = provinceService.getCoordinates(provinceName);

    mockRestServiceServer.verify();
    assertThat(provinces.get(0).getName()).isEqualTo("Tucumán");
    assertThat(provinces.get(0).getLatitude()).isEqualTo(-26.9478001830786);
    assertThat(provinces.get(0).getLongitude()).isEqualTo(-65.3647579441481);
  }


  @Test
  void getCoordinates_withAmbiguousProvinceName_returnProvincesListWithTwoResults() {
    String responseApi = getFileContent("api-gob-responses/api-gob-response-with-two-provinces.json");
    String provinceName = "Buenos Aires";
    ResponseCreator requestResponse = MockRestResponseCreators.withSuccess(responseApi, MediaType.APPLICATION_JSON);
    mockGetProvinceRequest(provinceName, requestResponse);

    List<Province> provinces = provinceService.getCoordinates(provinceName);

    mockRestServiceServer.verify();
    assertThat(provinces.size()).isEqualTo(2);
    assertThat(provinces.get(0).getName()).isEqualTo("Buenos Aires");
    assertThat(provinces.get(0).getLatitude()).isEqualTo(-36.6769415180527);
    assertThat(provinces.get(0).getLongitude()).isEqualTo(-60.5588319815719);
    assertThat(provinces.get(1).getName()).isEqualTo("Ciudad Autónoma de Buenos Aires");
    assertThat(provinces.get(1).getLatitude()).isEqualTo(-34.6144934119689);
    assertThat(provinces.get(1).getLongitude()).isEqualTo(-58.4458563545429);
  }

  @Test
  void getCoordinates_withInvalidProvinceName_returnEmptyProvincesList() {
    String responseApi = getFileContent("api-gob-responses/api-gob-response-with-zero-provinces.json");
    String provinceName = "123456";
    ResponseCreator requestResponse = MockRestResponseCreators.withSuccess(responseApi, MediaType.APPLICATION_JSON);
    mockGetProvinceRequest(provinceName, requestResponse);

    List<Province> provinces = provinceService.getCoordinates(provinceName);

    assertThat(provinces.isEmpty()).isTrue();
  }

  @Test
  void getCoordinates_withNullProvinceName_throwIllegalArgumentException() {
    String provinceName = null;

    assertThatIllegalArgumentException().isThrownBy(() -> provinceService.getCoordinates(provinceName));
  }

  @Test
  void getCoordinates_withEmptyProvinceName_throwIllegalArgumentException() {
    String provinceName = "";
    ResponseCreator requestResponse = MockRestResponseCreators.withBadRequest().contentType(MediaType.APPLICATION_JSON);
    mockGetProvinceRequest(provinceName, requestResponse);

    assertThatIllegalArgumentException()
            .isThrownBy(() -> provinceService.getCoordinates(provinceName))
            .withMessage("The province name do not have contents.");
  }

  private void mockGetProvinceRequest(String provinceName, ResponseCreator requestResponse) {
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
