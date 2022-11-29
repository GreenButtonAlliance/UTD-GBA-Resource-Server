/*
 *    Copyright (c) 2022 Green Button Alliance, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.usage.web.controller;

import java.util.regex.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.greenbuttonalliance.gbaresourceserver.usage.service.ApplicationInformationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Donald F. Coffin
 */
@WebMvcTest(controllers = ApplicationInformationController.class)
@ExtendWith(SpringExtension.class)
class ApplicationInformationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ApplicationInformationService applicationInformationService;

	@Test
	void testStatus()throws Exception{
		mockMvc.perform(get("/espi/1_1/resource/ApplicationInformation")
				.contentType("application/xml"))
			.andExpect(status().isOk());
	}


	//Verifies if the number of interval blocks recived is the same as exxpected
	//To use test update ApplicationInformation file the expectedValue
	@Test
	void getAll()throws Exception{
		//get xml output
		MvcResult mvcResult = mockMvc.perform(get("/espi/1_1/resource/ApplicationInformation")
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

		System.out.println(expectedValue == actualValue);
	}



	//Check if a certin ApplicationInformation exists in the XML output by checking for its UUID
	//To use test update ApplicationInformation file the ApplicationInformation
	@Test
	void getByUuid()throws Exception{
		String expectedUUID = "046b4788-f971-4662-b177-6d94832dd403";
		MvcResult mvcResult = mockMvc.perform(get("/espi/1_1/resource/ApplicationInformation")
				.contentType("application/xml"))
			.andReturn();

		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		System.out.println(actualResponseBody.contains(expectedUUID));
	}

}
