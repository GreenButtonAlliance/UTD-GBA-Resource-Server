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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 */
@Getter
@Setter
@Accessors(chain = true)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) //Was XmlAccessType.FIELD
public abstract class IdentifiedObjectDto implements Serializable {
	@XmlElement(name = "id")
	private UUID uuid;
	private String description;
	private String selfLinkHref;
	private String selfLinkRel;
	private String upLinkHref;
	private String upLinkRel;
	private LocalDateTime published;
	private LocalDateTime updated;

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public String addEntryWrapper(String title, String content) {
		return getEntryPrefix(title) + content + getEntrySuffix();
	}

	public static String addParentWrapper(String content, String type) {
		return getParentPrefix(type) + content + getParentSuffix();
	}

	public String getContent() {
		try {
			JAXBContext context = JAXBContext.newInstance(this.getClass());
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			mar.setProperty(Marshaller.JAXB_FRAGMENT, true);
			StringWriter stringWriter = new StringWriter();
			mar.marshal(this, stringWriter);
			return stringWriter.toString() + "\n";
		} catch (JAXBException e) {
			System.out.println(e);
		}
		return null;
	}

	private static String generateUUID(String content) {
		return nameUUIDFrom(content).toString();
	}

	/// <summary>
	/// Generates a type 5 UUID from a string
	// got from https://gist.github.com/sha1n/acca3b725c6d345b98672e709717abc6
	public static UUID nameUUIDFrom(String name) {
		MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
			sha1.update(name.getBytes("UTF-8"));
		} catch (Exception e) {
			System.out.println("Error generating UUID from name\n" + e);
			return null;
		}

		byte[] data = sha1.digest();
		data[6] = (byte) (data[6] & 0x0f);
		data[6] = (byte) (data[6] | 0x50); // set version 5
		data[8] = (byte) (data[8] & 0x3f);
		data[8] = (byte) (data[8] | 0x80);

		long msb = 0L;
		long lsb = 0L;

		for (int i = 0; i <= 7; i++)
			msb = (msb << 8) | (data[i] & 0xff);

		for (int i = 8; i <= 15; i++)
			lsb = (lsb << 8) | (data[i] & 0xff);

		long mostSigBits = msb;
		long leastSigBits = lsb;

		return new UUID(mostSigBits, leastSigBits);
	}


	private static String getParentPrefix(String type) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<feed xmlns=\"http://www.w3.org/2005/Atom\"\n" +
			"\txmlns:espi=\"http://naesb.org/espi\"\n" +
			"\txsi:schemaLocation=\"http://www.w3.org/2005/Atom https://greenbuttondata.org/xsd/beta/atom.xsd\n" +
			"\t\t\t\t\t\t  http://naesb.org/espi https://greenbuttondata.org/xsd/beta/usage.xsd\">\n" +
			"\t<id>urn:uuid:" + generateUUID(type) + "</id>\n" +
			"\t<title>Green Button Usage Feed</title>\n" +
			"\t<updated>" + dtf.format(LocalDateTime.now()) +"</updated>\n";
//			"\t\t<link rel=\"self\" href=\"https://sandbox.greenbuttonalliance.org:8443/DataCustodian/espi/1_1/resource/Subscription/4a795488\"  " +
//			"type=\"espi-entry/ " + type + "\" />\n";
	}

	private static String getParentSuffix() {
		return "</feed>";
	}

	private String getEntryPrefix(String title) {
		return "\t<entry>\n" +
			"\t\t<id>urn:uuid:"+ uuid +"</id> \n" +
			(selfLinkHref == null || selfLinkHref.isBlank() ?
				"\t\t<link rel=\"self\" href=\""+ selfLinkHref +"\" type=\"espi-entry/Subscription\" />\n" : "") +
			(upLinkHref == null || upLinkHref.isBlank() ?
				"\t\t<link rel=\"up\" href=\""+ upLinkHref +"\" type=\"espi-feed/UsagePoint\" />\n" : "") +
			(upLinkRel == null || upLinkRel.isBlank() ?
				"\t\t<link rel=\"related\" href=\"" + upLinkRel +"\" type=\"espi-entry/ElectricPowerQualitySummary\"  />\n" : "") +
			"\t\t<published>"+dtf.format(published)+"</published>\n" +
			"\t\t<updated>"+dtf.format(updated)+"</updated>\n" +
			"\t\t<title>"+ title +"</title>\n" +
			"\t\t<content>\n";
	}

	 private String getEntrySuffix() {
		return "\t\t</content>\n" +
			"\t</entry>\n";
	}

}
