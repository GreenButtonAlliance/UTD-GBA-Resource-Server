/*
 * Copyright (c) 2022 Green Button Alliance, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.greenbuttonalliance.gbaresourceserver.usage.web.dto;

import jakarta.persistence.Id;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IdentifiedObject;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;

import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 */
@Getter
@Setter
@Accessors(chain = true)
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD) //Was XmlAccessType.FIELD
public abstract class IdentifiedObjectDto implements Serializable {
	//@XmlElement
	private UUID uuid;
	private String description;
	private String selfLinkHref;
	private String selfLinkRel;
	private String upLinkHref;
	private String upLinkRel;
	private LocalDateTime published;
	private LocalDateTime updated;

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public String addWrapper(String content, boolean isEntry) {
		return getPrefix(isEntry) + content + getSuffix(isEntry);
	}

	public static String getContent(IdentifiedObjectDto identifiedObjectDto) {
		try {
			JAXBContext context = JAXBContext.newInstance(identifiedObjectDto.getClass());
			Marshaller mar = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			mar.marshal(identifiedObjectDto, stringWriter);
			return stringWriter.toString();
		} catch (JAXBException e) {
			System.out.println(e);
		}
		return null;
	}

	public String getContent() {
		try {
			JAXBContext context = JAXBContext.newInstance(this.getClass());
			Marshaller mar = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			mar.marshal(this, stringWriter);
			return stringWriter.toString();
		} catch (JAXBException e) {
			System.out.println(e);
		}
		return null;
	}

	public static String addParentWrapper(String content, String type) {
		return getParentPrefix(type) + content + getParentSuffix();
	}

	private static String generateUUID() {
		return UUID.randomUUID().toString();
	}


	private static String getParentPrefix(String type) {
		return " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
			"	<id>" + generateUUID() + "</id>\n" +
			"   <title>Green Button Usage Feed</title>\n" +
			"   <updated>" + dtf.format(LocalDateTime.now()) +"</updated>\n" +
			"   <link rel=\"self\" href=\"https://sandbox.greenbuttonalliance.org:8443/DataCustodian/espi/1_1/resource/Subscription/4a795488\"  " +
			"type=\"espi-entry/ " + type + "\" />\n";
	}

	private static String getParentSuffix() {
		return "</feed>";
	}

	private String getPrefix(boolean isEntry) {
		String s = "";
		if(!isEntry) {
			s += " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
				"	<id>"+uuid+"</id>\n" +
				"	<title>Green Button Usage Feed</title>\n" +
				"	<updated>" + dtf.format(updated) +"</updated>\n";
		}
		s +=" 	<entry xmlns:espi=\"http://naesb.org/espi\" xmlns=\"http://www.w3.org/2005/Atom\">\n" +
			"		<id>"+ uuid +"</id> \n" +
			"		<link rel=\"self\" href=\""+ selfLinkHref +"\" type=\"espi-entry/Subscription\" />\n" +
			"		<link rel=\"up\" href=\""+ upLinkHref +"\" type=\"espi-feed/UsagePoint\" />\n" +
			"		<link rel=\"related\" href=\"" + upLinkRel +"\" type=\"espi-entry/ElectricPowerQualitySummary\"  />\n" +
			"		<title>"+ description +"</title>\n" +
			"		<content>";
		return s;
	}

	 private String getSuffix(boolean isEntry) {
		return "		</content>\n" +
			"	<published>"+dtf.format(published)+"</published>\n" +
			"	<updated>"+dtf.format(updated)+"</updated>\n" +
			"	</entry>\n" + (isEntry ? "" : "</feed>");
	}

}
