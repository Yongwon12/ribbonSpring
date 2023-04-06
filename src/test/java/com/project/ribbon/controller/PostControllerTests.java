package com.project.ribbon.controller;

import ch.qos.logback.core.model.Model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ribbon.domain.post.PostWritementorDTO;
import com.project.ribbon.provide.JwtTokenProvider;
import com.project.ribbon.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private Model model;
    @MockBean
    private FirebaseCloudMessageLikedService firebaseCloudMessageLikedService;

    @MockBean
    private FirebaseCloudChatMessageService firebaseCloudChatMessageService;

    @MockBean
    private FirebaseCloudMessageCommentsService firebaseCloudMessageCommentsService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @Test
    public void testSaveWriteMultipart() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/post/writementor")
                        .param("title", "title")
                        .param("category", "category")
                        .param("shortcontent", "shortcontent")
                        .param("description", "description")
                        .param("career", "career")
                        .param("writedate","tlqkf")
                        .param("price", "1000")
                        .param("userid","4231")
                        .param("nickname","tlqkffja")
                        .param("region", "region")
                        .param("contacttime", "contacttime")
                        // 인증 헤더 설정
                        .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJleHAiOjE2ODA3MjA1Mzd9.58pCOvR19n7ezPJOscHXmqv2yqdM8nvcOmpYbnkR0pA"))
                .andExpect(status().isOk());
    }
    @Test
    public void testSaveWritePost() throws Exception {
        // JSON 형태의 요청 본문 데이터
        String jsonBody = "{\"title\": \"title\", \"category\": \"category\", \"shortcontent\": \"shortcontent\", " +
                "\"description\": \"description\", \"career\": \"career\", \"writedate\": \"tlqkf\", " +
                "\"price\": 1000, \"userid\": \"4231\", \"nickname\": \"tlqkffja\", \"region\": \"region\", " +
                "\"contacttime\": \"contacttime\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/writementor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        // 인증 헤더 설정
                        .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJleHAiOjE2ODA3MjA1Mzd9.58pCOvR19n7ezPJOscHXmqv2yqdM8nvcOmpYbnkR0pA"))
                .andExpect(status().isOk());
    }

    @Test
    public void testMentorWriteOne() throws Exception {
        // Given
        Long postId = 1L;
        PostWritementorDTO params = new PostWritementorDTO();
        params.setId(postId);

        List<PostWritementorDTO> mockPosts = new ArrayList<>();
        mockPosts.add(new PostWritementorDTO());
        when(postService.findMentorOnePost(postId)).thenReturn(mockPosts);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/writementor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJleHAiOjE2ODA1NDczNzJ9.2_9HT9vstB8honOfYqbPFnNP0V9YjlmUH0Uz4jbkKt0")
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk()).andReturn();
        MockHttpServletResponse response = result.getResponse();



        // Then
        String responseContent = response.getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});
        List<PostWritementorDTO> posts = (List<PostWritementorDTO>) responseMap.get("mentorwrite");
        assertEquals(mockPosts, posts);
    }

    @Test
    public void testMyWriteMentor() throws Exception {
        // Given
        Long userId = 12L;
        PostWritementorDTO params = new PostWritementorDTO();
        params.setUserid(userId);

        List<PostWritementorDTO> mockPosts = new ArrayList<>();
        mockPosts.add(new PostWritementorDTO());
        when(postService.findMentorPostByMyUserId(userId)).thenReturn(mockPosts);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/post/mywritementor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJleHAiOjE2ODA1NDczNzJ9.2_9HT9vstB8honOfYqbPFnNP0V9YjlmUH0Uz4jbkKt0")
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk()).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Then
        String responseContent = response.getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});
        List<PostWritementorDTO> posts = (List<PostWritementorDTO>) responseMap.get("mymentor");
        assertEquals(mockPosts, posts);
    }

}

