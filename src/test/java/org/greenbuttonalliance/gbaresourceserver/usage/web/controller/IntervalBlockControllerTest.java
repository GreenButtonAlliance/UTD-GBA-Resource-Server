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


import java.io.*;
import java.net.*;
import java.io.IOException;
import java.util.regex.*;
import java.util.stream.Collectors;


import org.junit.jupiter.api.Test;


/**
 * @author Donald F. Coffin
 */
class IntervalBlockControllerTest {


//Verifies if the number of interval blocks recived is the same as exxpected
//To use test update IntervalBlock file the expectedValue
	@Test
	void getAll()throws IOException {
		int expectedValue = 20;
		int actualValue = 0;


		URL website = new URL("http://localhost:8080/espi/1_1/resource/IntervalBlock");
		URLConnection yc = website.openConnection();
		BufferedReader in = new BufferedReader(
			new InputStreamReader(
				yc.getInputStream()));
		String xml = in.lines().collect(Collectors.joining("\n"));
		in.close();

		Pattern p = Pattern.compile("<id>");
		Matcher m = p.matcher( xml );
		while (m.find()) {
			actualValue++;
		}

		System.out.println(expectedValue == actualValue);
	}


	//Check if a certin intervalblock exists in the XML output by checking for its UUID
	//To use test update IntervalBlock file the IntervalBlock
	@Test
	void getByUuid() throws IOException {
		String expectedUUID = "046b4788-f971-4662-b177-6d94832dd403";
		URL website = new URL("http://localhost:8080/espi/1_1/resource/IntervalBlock");
		URLConnection yc = website.openConnection();
		BufferedReader in = new BufferedReader(
			new InputStreamReader(
				yc.getInputStream()));
		String inputLine = in.lines().collect(Collectors.joining("\n"));
		in.close();

		System.out.println(inputLine.contains(expectedUUID));
	}

}
