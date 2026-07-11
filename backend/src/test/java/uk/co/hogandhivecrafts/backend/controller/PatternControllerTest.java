package uk.co.hogandhivecrafts.backend.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;
import uk.co.hogandhivecrafts.backend.configuration.CorsProperties;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsRequest;
import uk.co.hogandhivecrafts.backend.dto.GetAllPatternsResponse;
import uk.co.hogandhivecrafts.backend.dto.GetPatternByIdResponse;
import uk.co.hogandhivecrafts.backend.exception.PatternNotFoundException;
import uk.co.hogandhivecrafts.backend.service.PatternService;
import uk.co.hogandhivecrafts.backend.support.assertions.PatternsDtoAssertions;
import uk.co.hogandhivecrafts.backend.support.testdata.PatternsDtoTestData;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = PatternController.class)
@Import(CorsProperties.class)
class PatternControllerTest {
    private static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatternService patternService;

    @Test
    void getAllPatterns_noPatterns_returns200AndEmptyList() throws Exception {
        GetAllPatternsResponse expected = PatternsDtoTestData.buildEmptyGetAllPatternsResponse();
        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        GetAllPatternsResponse actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), GetAllPatternsResponse.class);

        PatternsDtoAssertions.assertGetAllPatternsResponseEquals(actual, expected);
    }

    @Test
    void getAllPatterns_patternsExist_returns200AndPagedResponse() throws Exception {
        GetAllPatternsResponse expected = PatternsDtoTestData.buildDefaultGetAllPatternsResponse(List.of(UUID.randomUUID(), UUID.randomUUID()));

        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        GetAllPatternsResponse actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), GetAllPatternsResponse.class);

        PatternsDtoAssertions.assertGetAllPatternsResponseEquals(actual, expected);
    }

    @Test
    void getAllPatterns_negativePage_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1").param("page", "-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Page must be greater than or equal to 0"));
    }

    @Test
    void getAllPatterns_negativeSize_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1").param("size", "0"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Size must be greater than or equal to 1"));
    }

    @Test
    void getAllPatterns_largeSize_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1").param("size", "101"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Size must be less than or equal to 100"));
    }

    @Test
    void getAllPatterns_invalidSortDirection_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1").param("sortDirection", "INVALID"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Invalid value for 'sortDirection': INVALID. Allowed values: ASC, DESC"));
    }

    @Test
    void getAllPatterns_invalidPatternSortField_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1").param("sortField", "INVALID"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Invalid value for 'sortField': INVALID. Allowed values: ID, NAME, CREATED_AT, UPDATED_AT"));
    }

    @Test
    void getAllPatterns_withRequestParams_passesParamsToService() throws Exception {
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        GetAllPatternsRequest expected = PatternsDtoTestData.buildDefaultGetAllPatternsRequest();
        ArgumentCaptor<GetAllPatternsRequest> captor = ArgumentCaptor.forClass(GetAllPatternsRequest.class);

        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1")
                .param("page", "1")
                .param("size", "10")
                .param("sortField", "NAME")
                .param("sortDirection", "DESC")).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(patternService).getAllPatterns(captor.capture());
        GetAllPatternsRequest actual = captor.getValue();

        PatternsDtoAssertions.assertGetAllPatternsRequestEquals(actual, expected);
    }

    @Test
    void getAllPatterns_noRequestParams_usesDefaults() throws Exception {
        GetAllPatternsResponse response = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        GetAllPatternsRequest expected = PatternsDtoTestData.buildEmptyGetAllPatternsRequest();
        ArgumentCaptor<GetAllPatternsRequest> captor = ArgumentCaptor.forClass(GetAllPatternsRequest.class);

        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(patternService).getAllPatterns(captor.capture());
        GetAllPatternsRequest actual = captor.getValue();

        PatternsDtoAssertions.assertGetAllPatternsRequestEquals(actual, expected);
    }

    @Test
    void getPatternById_patternExists_returns200AndPattern() throws Exception {
        GetPatternByIdResponse expected = PatternsDtoTestData.buildDefaultGetPatternByIdResponse();

        BDDMockito.given(patternService.getPatternById(ArgumentMatchers.any(UUID.class))).willReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/patterns/v1/%s", DEFAULT_ID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        GetPatternByIdResponse actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), GetPatternByIdResponse.class);

        PatternsDtoAssertions.assertGetPatternByIdResponseEquals(actual, expected);
    }

    @Test
    void getPatternById_patternNotFound_returns404() throws Exception {
        BDDMockito.given(patternService.getPatternById(ArgumentMatchers.any(UUID.class)))
                .willThrow(new PatternNotFoundException(DEFAULT_ID));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/patterns/v1/%s", DEFAULT_ID)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Pattern not found with ID: 00000000-0000-0000-0000-000000000000"));
    }

    @Test
    void getPatternById_invalidId_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1/INVALID"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Invalid value for ID path parameter: INVALID (expected UUID)"));
    }

    @Test
    void deletePatternById_patternExists_returns204() throws Exception {
        BDDMockito.doNothing()
                .when(patternService).deletePatternById(ArgumentMatchers.any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/patterns/v1/%s", DEFAULT_ID)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void deletePatternById_patternNotFound_returns404() throws Exception {
        BDDMockito.willThrow(new PatternNotFoundException(DEFAULT_ID))
                .given(patternService).deletePatternById(ArgumentMatchers.any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/patterns/v1/%s", DEFAULT_ID)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Pattern not found with ID: 00000000-0000-0000-0000-000000000000"));
    }

    @Test
    void deletePatternById_invalidId_returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patterns/v1/INVALID"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Invalid value for ID path parameter: INVALID (expected UUID)"));
    }
}
