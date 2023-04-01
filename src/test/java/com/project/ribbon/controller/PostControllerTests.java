package com.project.ribbon.controller;

import com.project.ribbon.provide.JwtTokenProvider;
import com.project.ribbon.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private FirebaseCloudMessageLikedService firebaseCloudMessageLikedService;
    @MockBean
    private FirebaseCloudChatMessageService firebaseCloudChatMessageService;
    @MockBean
    private FirebaseCloudMessageCommentsService firebaseCloudMessageCommentsService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;



    @Value("${mentor.image.ip}")
    private String mentorIp;

    @Test
    public void testSaveWritementorPost() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJleHAiOjE2ODA0MzE4NTJ9.v1gG_Tom80DOgAIE1JTxBYhyhX0cmEHAfNwekEylMuE";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/post/writementor")
                        .header("Authorization", "Bearer " + token)
                        .param("title", "title")
                        .param("category", "category")
                        .param("shortcontent", "shortcontent")
                        .param("description", "description")
                        .param("carrer", "carrer")
                        .param("price", "1000")
                        .param("region", "region")
                        .param("contacttime", "contacttime"))
                .andExpect(status().isOk());
    }


}
