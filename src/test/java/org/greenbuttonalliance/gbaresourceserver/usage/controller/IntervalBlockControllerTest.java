/*
 * Copyright (c) 2022-2024 Green Button Alliance, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.usage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.greenbuttonalliance.gbaresourceserver.usage.service.IntervalBlockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Donald F. Coffin
 */
@Testcontainers
@WebMvcTest(controllers = IntervalBlockController.class)
@ExtendWith(SpringExtension.class)
class IntervalBlockControllerTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private IntervalBlockService intervalBlockService;

	@Test
	void connectionEstablished() {
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
	}

	@Test
	void testStatus()throws Exception{
		mockMvc.perform(get("/espi/1_1/resource/IntervalBlock")
				.contentType("application/xml"))
			.andExpect(status().isOk());
	}

	//Verifies if the number of interval blocks received is the same as expected
	//To use test update IntervalBlock file the expectedValue
	@Test
	void getAll()throws Exception{
		//get xml output
		MvcResult mvcResult = mockMvc.perform(get("/espi/1_1/resource/IntervalBlock")
				.contentType("application/xml"))
			.andReturn();

		String actualResponseBody = mvcResult.getResponse().getContentAsString();


		//counts the number of times the <id> tag in the xml output. If its the same as the expected value then it returns true.
		int expectedValue = 20;
		int actualValue = 0;
		Pattern p = Pattern.compile("<id>");
		Matcher m = p.matcher( actualResponseBody );
		while (m.find()) {
			actualValue++;
		}

		//TODO: Add assertion test
		System.out.println(expectedValue == actualValue);

	}



	//Check if a certain intervalblock exists in the XML output by checking for its UUID
	//To use test update IntervalBlock file the IntervalBlock
	@Test
	void getByUuid()throws Exception{
		String expectedUUID = "046b4788-f971-4662-b177-6d94832dd403";
		MvcResult mvcResult = mockMvc.perform(get("/espi/1_1/resource/IntervalBlock")
				.contentType("application/xml"))
			.andReturn();

		//TODO: Add assertion test
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		System.out.println(actualResponseBody.contains(expectedUUID));
	}
}
