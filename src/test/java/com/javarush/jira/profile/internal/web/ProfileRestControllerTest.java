package com.javarush.jira.profile.internal.web;


import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.user;
import static com.javarush.jira.profile.internal.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.internal.web.ProfileTestData.PROFILE_MATCHER;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getUpdated;
import static com.javarush.jira.profile.internal.web.ProfileTestData.getUpdatedTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class ProfileRestControllerTest extends AbstractControllerTest {

  @Autowired
  private ProfileRepository profileRepository;
  @Test
  @WithUserDetails(value = USER_MAIL)
  void get() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"));
  }
  @Test
  void getUnAuth() throws Exception {
    perform(MockMvcRequestBuilders.get(REST_URL))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails(value = USER_MAIL)
  void update() throws Exception {

    perform(MockMvcRequestBuilders.put(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(writeValue(getUpdatedTo())))
        .andDo(print())
        .andExpect(status().isNoContent());

    Profile profileAfter = profileRepository.getOrCreate(user.id());

    PROFILE_MATCHER.assertMatch(profileAfter, getUpdated(user.id()));
  }

}