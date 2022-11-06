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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Donald F. Coffin
 */
class IntervalBlockControllerTest {


//Verifies if the number of interval blocks recived is the same as exxpected
//To use test update IntervalBlock file the expectedValue
	@Test
	void getAll() {
		int expectedValue = 20;
		int actualValue = 0;


		try {
			//find and format the xml file, which is found in the same folder as this file and is named IntervalBlock
			File file = new File("C:\\Users\\suvan\\Desktop\\seniorDesign\\GBA-Resource-Server\\src\\test\\java\\org\\greenbuttonalliance\\gbaresourceserver\\usage\\web\\controller\\IntervalBlock");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			//Counts the number of elements with the tag ID
			actualValue = doc.getElementsByTagName("id").getLength();


		}
		catch (Exception e) {
		e.printStackTrace();
		}

		//checks if the number of ID tags is the same as in the output file.
		//Each interval block has one ID so counting for IDs will tell us the number of interval blocks
		if(expectedValue==actualValue){
			System.out.println("True");
		}
		else{
			System.out.println("False");
		}
	}

	//Check if a certin intervalblock exists in the XML output by checking for its UUID
	//To use test update IntervalBlock file the IntervalBlock
	@Test
	void getByUuid() throws IOException {
		String expectedUUID = "65866e0d-4a9b-555b-8d38-ef2d9d51fc11";
		Boolean foundUUID = false;

		try {
			//find and format the xml file, which is found in the same folder as this file and is named IntervalBlock
			File file = new File("C:\\Users\\suvan\\Desktop\\seniorDesign\\GBA-Resource-Server\\src\\test\\java\\org\\greenbuttonalliance\\gbaresourceserver\\usage\\web\\controller\\IntervalBlock");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			//Counts the number of elements with the tag ID
			int length  = doc.getElementsByTagName("id").getLength();

			//Loops though them to find if one of recived UUIDS is the same as the expectedUUID
			for(int i=0;i<length-1;i++){
				String UUID1 = doc.getElementsByTagName("id").item(i).getTextContent();
				if(UUID1.contains(expectedUUID)){
					foundUUID = true;
				}
			}


		}
		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(foundUUID);
	}

}
