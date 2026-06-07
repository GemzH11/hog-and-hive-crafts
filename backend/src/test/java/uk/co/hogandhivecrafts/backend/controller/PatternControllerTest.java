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
import uk.co.hogandhivecrafts.backend.service.PatternService;
import uk.co.hogandhivecrafts.backend.support.assertions.PatternsDtoAssertions;
import uk.co.hogandhivecrafts.backend.support.testdata.PatternsDtoTestData;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = PatternController.class)
@Import(CorsProperties.class)
public class PatternControllerTest {
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

        GetAllPatternsResponse actual = parseResponse(result, GetAllPatternsResponse.class);

        PatternsDtoAssertions.assertGetAllPatternsResponseEquals(actual, expected);
    }

    @Test
    void getAllPatterns_patternsExist_returns200AndPagedResponse() throws Exception {
        GetAllPatternsResponse expected =
                PatternsDtoTestData.buildDefaultGetAllPatternsResponse(List.of(UUID.randomUUID(), UUID.randomUUID()));

        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        GetAllPatternsResponse actual = parseResponse(result, GetAllPatternsResponse.class);

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
    void getAllPatterns_withRequestParams_passesParamsToService() throws Exception {
        GetAllPatternsResponse expected = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        ArgumentCaptor<GetAllPatternsRequest> captor = ArgumentCaptor.forClass(GetAllPatternsRequest.class);

        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(patternService).getAllPatterns(captor.capture());
        GetAllPatternsRequest actual = captor.getValue();

        PatternsDtoAssertions.assertGetAllPatternsRequestEquals(actual, new GetAllPatternsRequest(1, 10));
    }

    @Test
    void getAllPatterns_noRequestParams_usesDefaults() throws Exception {
        GetAllPatternsResponse expected = PatternsDtoTestData.buildDefaultGetAllPatternsResponse();
        ArgumentCaptor<GetAllPatternsRequest> captor = ArgumentCaptor.forClass(GetAllPatternsRequest.class);

        BDDMockito.given(patternService.getAllPatterns(ArgumentMatchers.any(GetAllPatternsRequest.class)))
                .willReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patterns/v1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(patternService).getAllPatterns(captor.capture());
        GetAllPatternsRequest request = captor.getValue();

        PatternsDtoAssertions.assertGetAllPatternsRequestEquals(request,
                new GetAllPatternsRequest(null, null));
    }

    private <T> T parseResponse(MvcResult result, Class<T> objectType) throws UnsupportedEncodingException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), objectType);
    }

}
