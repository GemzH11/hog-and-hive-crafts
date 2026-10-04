package uk.co.hogandhivecrafts.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;
import uk.co.hogandhivecrafts.backend.configuration.CorsProperties;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternRequest;
import uk.co.hogandhivecrafts.backend.dto.pattern.PostPatternResponse;
import uk.co.hogandhivecrafts.backend.exception.CustomErrorResponse;
import uk.co.hogandhivecrafts.backend.exception.PatternNotFoundException;
import uk.co.hogandhivecrafts.backend.model.PatternSortField;
import uk.co.hogandhivecrafts.backend.service.PatternService;
import uk.co.hogandhivecrafts.backend.support.assertions.PatternDtoAssertions;
import uk.co.hogandhivecrafts.backend.support.testdata.PatternDtoTestData;
import uk.co.hogandhivecrafts.backend.support.testdata.TestDataConstants;

/**
 * Tests the pattern controller's HTTP mappings, response payloads, and request validation.
 */
@WebMvcTest(controllers = PatternController.class)
@Import(CorsProperties.class)
class PatternControllerTest {
  private static final String BASE_URL = "/api/patterns";
  private static final String RESOURCE_URL_TEMPLATE = BASE_URL + "/%s";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private PatternService patternService;

  @Test
  void getAllPatterns_noPatterns_returns200AndEmptyList() throws Exception {
    GetAllPatternsResponse expected = new GetAllPatternsResponse(List.of(), 0, 0, 0,
                                                                 TestDataConstants.DEFAULT_PAGE_SIZE);
    when(patternService.getAllPatterns(any(GetAllPatternsRequest.class))).thenReturn(expected);

    String responseBody = mockMvc.perform(get(BASE_URL))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    GetAllPatternsResponse response = objectMapper.readValue(responseBody,
                                                             GetAllPatternsResponse.class);

    PatternDtoAssertions.assertGetAllPatternsResponseMatches(expected, response);
    verify(patternService).getAllPatterns(any(GetAllPatternsRequest.class));
  }

  @Test
  void getAllPatterns_patternsExist_returns200AndPagedResponse() throws Exception {
    GetAllPatternsResponse expected = new GetAllPatternsResponse(
        List.of(PatternDtoTestData.buildGetAllPatternsItem(0),
                PatternDtoTestData.buildGetAllPatternsItem(1)), 2, 1, 0,
        TestDataConstants.DEFAULT_PAGE_SIZE);
    when(patternService.getAllPatterns(any())).thenReturn(expected);

    String responseBody = mockMvc.perform(get(BASE_URL))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    GetAllPatternsResponse response = objectMapper.readValue(responseBody,
                                                             GetAllPatternsResponse.class);

    PatternDtoAssertions.assertGetAllPatternsResponseMatches(expected, response);
    verify(patternService).getAllPatterns(any());
  }

  @Test
  void getAllPatterns_negativePage_returns400() throws Exception {
    String responseBody = mockMvc.perform(get(BASE_URL).param("page", "-1"))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertInvalidRequestMatches(response, BASE_URL,
                                                     "Page must be greater than or equal to 0");
    verifyNoInteractions(patternService);
  }

  @Test
  void getAllPatterns_negativeSize_returns400() throws Exception {
    String responseBody = mockMvc.perform(get(BASE_URL).param("size", "0"))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    assertInvalidRequest(responseBody, BASE_URL, "Size must be greater than or equal to 1");
    verifyNoInteractions(patternService);
  }

  @Test
  void getAllPatterns_largeSize_returns400() throws Exception {
    String responseBody = mockMvc.perform(get(BASE_URL).param("size", "101"))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    assertInvalidRequest(responseBody, BASE_URL, "Size must be less than or equal to 100");
    verifyNoInteractions(patternService);
  }

  @Test
  void getAllPatterns_invalidSortDirection_returns400() throws Exception {
    String responseBody = mockMvc.perform(get(BASE_URL).param("sortDirection", "INVALID"))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    assertInvalidRequest(responseBody, BASE_URL, "Invalid value for 'sortDirection': INVALID");
    verifyNoInteractions(patternService);
  }

  @Test
  void getAllPatterns_invalidPatternSortField_returns400() throws Exception {
    String responseBody = mockMvc.perform(get(BASE_URL).param("sortField", "INVALID"))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    assertInvalidRequest(responseBody, BASE_URL, "Invalid value for 'sortField': INVALID");
    verifyNoInteractions(patternService);
  }

  @Test
  void getAllPatterns_withRequestParams_passesParamsToService() throws Exception {
    GetAllPatternsResponse expectedResponse = new GetAllPatternsResponse(
        List.of(PatternDtoTestData.buildGetAllPatternsItem(0),
                PatternDtoTestData.buildGetAllPatternsItem(1)), 12, 3, 2, 5);
    when(patternService.getAllPatterns(any())).thenReturn(expectedResponse);

    String responseBody = mockMvc.perform(get(BASE_URL).param("page", "2")
                                                       .param("size", "5")
                                                       .param("sortField", "NAME")
                                                       .param("sortDirection", "DESC"))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    GetAllPatternsResponse response = objectMapper.readValue(responseBody,
                                                             GetAllPatternsResponse.class);
    ArgumentCaptor<GetAllPatternsRequest> requestCaptor = ArgumentCaptor.forClass(
        GetAllPatternsRequest.class);

    PatternDtoAssertions.assertGetAllPatternsResponseMatches(expectedResponse, response);
    verify(patternService).getAllPatterns(requestCaptor.capture());
    Assertions.assertThat(requestCaptor.getValue())
              .isEqualTo(
                  new GetAllPatternsRequest(2, 5, PatternSortField.NAME, Sort.Direction.DESC));
  }

  @Test
  void getAllPatterns_noRequestParams_usesDefaults() throws Exception {
    GetAllPatternsResponse expected = new GetAllPatternsResponse(
        List.of(PatternDtoTestData.buildGetAllPatternsItem(0),
                PatternDtoTestData.buildGetAllPatternsItem(1)), 2, 1,
        TestDataConstants.DEFAULT_PAGE_NUMBER, TestDataConstants.DEFAULT_PAGE_SIZE);
    when(patternService.getAllPatterns(any())).thenReturn(expected);

    String responseBody = mockMvc.perform(get(BASE_URL))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    GetAllPatternsResponse response = objectMapper.readValue(responseBody,
                                                             GetAllPatternsResponse.class);
    ArgumentCaptor<GetAllPatternsRequest> requestCaptor = ArgumentCaptor.forClass(
        GetAllPatternsRequest.class);

    PatternDtoAssertions.assertGetAllPatternsResponseMatches(expected, response);
    verify(patternService).getAllPatterns(requestCaptor.capture());
    Assertions.assertThat(requestCaptor.getValue())
              .isEqualTo(new GetAllPatternsRequest(null, null, null, null));
  }

  @Test
  void getPatternById_patternExists_returns200AndPattern() throws Exception {
    UUID patternId = UUID.fromString(String.format(TestDataConstants.PATTERN_ID_STRING, 0));
    GetPatternByIdResponse expected = PatternDtoTestData.buildGetPatternByIdResponse(0);
    when(patternService.getPatternById(patternId)).thenReturn(expected);

    String responseBody = mockMvc.perform(get(RESOURCE_URL_TEMPLATE.formatted(patternId)))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    GetPatternByIdResponse response = objectMapper.readValue(responseBody,
                                                             GetPatternByIdResponse.class);

    PatternDtoAssertions.assertGetPatternByIdResponseMatches(expected, response);
    verify(patternService).getPatternById(patternId);
  }

  @Test
  void getPatternById_patternNotFound_returns404() throws Exception {
    UUID patternId = TestDataConstants.DEFAULT_ID;
    when(patternService.getPatternById(patternId)).thenThrow(
        new PatternNotFoundException(patternId));

    String responseBody = mockMvc.perform(get(RESOURCE_URL_TEMPLATE.formatted(patternId)))
                                 .andExpect(status().isNotFound())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertResourceNotFoundMatches(response,
                                                       RESOURCE_URL_TEMPLATE.formatted(patternId),
                                                       String.format(
                                                           PatternNotFoundException.PATTERN_NOT_FOUND,
                                                           patternId));
    verify(patternService).getPatternById(patternId);
  }

  @Test
  void getPatternById_invalidId_returns400() throws Exception {
    String responseBody = mockMvc.perform(get(RESOURCE_URL_TEMPLATE.formatted("INVALID")))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    assertInvalidRequest(responseBody, RESOURCE_URL_TEMPLATE.formatted("INVALID"),
                         "Invalid value for 'id': INVALID");
    verifyNoInteractions(patternService);
  }

  @Test
  void deletePatternById_patternExists_returns204() throws Exception {
    UUID patternId = UUID.fromString(String.format(TestDataConstants.PATTERN_ID_STRING, 0));

    mockMvc.perform(delete(RESOURCE_URL_TEMPLATE.formatted(patternId)))
           .andExpect(status().isNoContent())
           .andExpect(content().string(""));

    verify(patternService).deletePatternById(patternId);
  }

  @Test
  void deletePatternById_patternNotFound_returns404() throws Exception {
    UUID patternId = TestDataConstants.DEFAULT_ID;
    doThrow(new PatternNotFoundException(patternId)).when(patternService)
                                                    .deletePatternById(patternId);

    String responseBody = mockMvc.perform(delete(RESOURCE_URL_TEMPLATE.formatted(patternId)))
                                 .andExpect(status().isNotFound())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertResourceNotFoundMatches(response,
                                                       RESOURCE_URL_TEMPLATE.formatted(patternId),
                                                       String.format(
                                                           PatternNotFoundException.PATTERN_NOT_FOUND,
                                                           patternId));
    verify(patternService).deletePatternById(patternId);
  }

  @Test
  void deletePatternById_invalidId_returns400() throws Exception {
    String responseBody = mockMvc.perform(delete(RESOURCE_URL_TEMPLATE.formatted("INVALID")))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    assertInvalidRequest(responseBody, RESOURCE_URL_TEMPLATE.formatted("INVALID"),
                         "Invalid value for 'id': INVALID");
    verifyNoInteractions(patternService);
  }

  @Test
  void savePattern_allProperties_returns201AndPatternId() throws Exception {
    PostPatternRequest request = PatternDtoTestData.buildPostPatternRequest(0);
    PostPatternResponse expected = new PostPatternResponse(
        UUID.fromString(String.format(TestDataConstants.PATTERN_ID_STRING, 0)));
    when(patternService.savePattern(request)).thenReturn(expected);

    MvcResult result = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                     .content(
                                                         objectMapper.writeValueAsString(request)))
                              .andExpect(status().isCreated())
                              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                              .andReturn();
    PostPatternResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                                                          PostPatternResponse.class);

    PatternDtoAssertions.assertPostPatternResponseMatches(expected, response);
    Assertions.assertThat(result.getResponse().getHeader("Location"))
              .isEqualTo(RESOURCE_URL_TEMPLATE.formatted(expected.id()));
    verify(patternService).savePattern(request);
  }

  @Test
  void savePattern_onlyMandatory_returns201AndPatternId() throws Exception {
    PostPatternRequest request = new PostPatternRequest(
        String.format(TestDataConstants.PATTERN_NAME, 0), null,
        TestDataConstants.PATTERN_CRAFT_TYPE, null);
    PostPatternResponse expected = new PostPatternResponse(
        UUID.fromString(String.format(TestDataConstants.PATTERN_ID_STRING, 0)));
    when(patternService.savePattern(request)).thenReturn(expected);

    MvcResult result = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                     .content(
                                                         objectMapper.writeValueAsString(request)))
                              .andExpect(status().isCreated())
                              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                              .andReturn();
    PostPatternResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                                                          PostPatternResponse.class);

    PatternDtoAssertions.assertPostPatternResponseMatches(expected, response);
    Assertions.assertThat(result.getResponse().getHeader("Location"))
              .isEqualTo(RESOURCE_URL_TEMPLATE.formatted(expected.id()));
    verify(patternService).savePattern(request);
  }

  @Test
  void savePattern_noName_returns400() throws Exception {
    PostPatternRequest request = new PostPatternRequest(null, String.format(
        TestDataConstants.PATTERN_SOURCE, 0), TestDataConstants.PATTERN_CRAFT_TYPE, String.format(
        TestDataConstants.PATTERN_NOTES, 0));

    String responseBody = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(
                                                            request)))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertInvalidRequestMatches(response, BASE_URL, "Name is required");
    verifyNoInteractions(patternService);
  }

  @Test
  void savePattern_invalidCraftType_returns400() throws Exception {
    String invalidRequest = """
        {"name":"%s","source":null,"craftType":"INVALID","notes":null}
        """.formatted(String.format(TestDataConstants.PATTERN_NAME, 0));

    String responseBody = mockMvc.perform(
                                     post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(invalidRequest))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertInvalidRequestMatches(response, BASE_URL,
                                                     "Malformed JSON request body");
    verifyNoInteractions(patternService);
  }

  @Test
  void savePattern_largeName_returns400() throws Exception {
    String largeName = "A".repeat(129);
    PostPatternRequest request = new PostPatternRequest(largeName, String.format(
        TestDataConstants.PATTERN_SOURCE, 0), TestDataConstants.PATTERN_CRAFT_TYPE, String.format(
        TestDataConstants.PATTERN_NOTES, 0));

    String responseBody = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(
                                                            request)))
                                 .andExpect(status().isBadRequest())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertInvalidRequestMatches(response, BASE_URL,
                                                     "Name must be 128 characters or fewer");
    verifyNoInteractions(patternService);
  }

  private void assertInvalidRequest(String responseBody, String expectedPath,
                                    String expectedError) {
    CustomErrorResponse response = objectMapper.readValue(responseBody, CustomErrorResponse.class);

    PatternDtoAssertions.assertInvalidRequestMatches(response, expectedPath, expectedError);
  }
}
