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

package org.greenbuttonalliance.gbaresourceserver.usage.web.controller;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.service.IntervalBlockService;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.IntervalBlockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Just a starting point for the API team, feel free to modify/delete as needed
 *
 * TODO All entities returned by controllers need to conform to the NAESB standard. This includes the Atom schema, and means entities need to be wrapped with:
 *  Prefix:
 *      <?xml version="1.0" encoding="UTF-8"?>
 *          <?xml-stylesheet type="text/xsl" href="GreenButtonDataStyleSheet.xslt"?>       {The href= entry should be a valid URL in production.  This may be omitted for the current project}
 *          <feed xmlns="http://www.w3.org/2005/Atom" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 *              <id>urn:uuid:046b4788-f971-4662-b177-6d94832dd403</id>       {This is a unique UUID version 3 or 5 value calculated at the time the API is requested}
 *              <title>Green Button Usage Feed</title>                                                {This is a constant created for each API but MUST not contain Personal Identifiable Information (PII)}
 *              <updated>{lastUpdateDate}</updated>                                                 {UTC timestamp normally the time the API response is issued}
 *  Entity being exported:
 *      <entry xmlns:espi="http://naesb.org/espi" xmlns="http://www.w3.org/2005/Atom">
 *          <id>{uuid}</id>                                                                                          {resource Index UUID value}
 *          <link rel="{selfLinkRel}" href="{selfLinkHref}" type="espi-entry/{resourceName}" />                    {This is ESPI Resource name}
 *          <link rel="{upLinkRel}" href="{upLinkHref}" type="espi-feed/{resourceName}" />
 *          <link rel="related" href="{selfLinkHref of related Resources}" type="espi-entry/{ResourceName}"  />  {While the type= is usually a xxxx-entry it could be a collection and would be xxxx-feed}
 *          <title>Description</tltle>                                                                        {Required element, but it can be empty}
 *          <content>
 *  Suffix:
 *      </content>
 *      <published>{publicationDate}</updated>                                              {UTC timestamp normally the time the API response is issued}
 *      <updated>{lastUpdateDate}</updated>                                                 {UTC timestamp normally the time the API response is issued}
 *      </entry>
 *      </feed>                                                                                                       {One or more entries can occur in a <feed><feed>
 *  Need to investigate the best way to make this happen: class annotations, a utils file, or something else?
 */
@RestController
@Transactional
@Slf4j
@RequestMapping(path = "/espi/1_1/resource/IntervalBlock", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IntervalBlockController {
	private final IntervalBlockService intervalBlockService;
	private static final SimpleDateFormat publicationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final SimpleDateFormat lastUpdateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	//uuid needs to be changed
	private String prefix = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"           <?xml-stylesheet type=\"text/xsl\" href=\"GreenButtonDataStyleSheet.xslt\"?>\n" +
		"           <feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
		"               <id>urn:uuid:046b4788-f971-4662-b177-6d94832dd403</id>\n" +
		"               <title>Green Button Usage Feed</title>\n" +
		"               <updated>" + lastUpdateDate +"</updated>\n" +
		"   Entity being exported:\n" +
		"       <entry xmlns:espi=\"http://naesb.org/espi\" xmlns=\"http://www.w3.org/2005/Atom\">\n" +
		"           <id>{uuid}</id>                                                                                          {resource Index UUID value}\n" +
		"           <link rel=\"self\" href=\"https://sandbox.greenbuttonalliance.org:8443/DataCustodian/espi/1_1/resource/Subscription/4a795488\" type=\"espi-entry/Subscription\" />\n" +
		"           <link rel=\"up\" href=\"https://sandbox.greenbuttonalliance.org:8443/DataCustodian/espi/1_1/resource/RetailCustomer/92770a14/UsagePoint\" type=\"espi-feed/UsagePoint\" />\n" +
		"          <link rel=\"related\" href=\"https://sandbox.greenbuttonalliance.org:8443/DataCustodian/espi/1_1/resource/RetailCustomer/92770a14/ElectricPowerQualitySummary/0b1d2485\" type=\"espi-entry/ElectricPowerQualitySummary\"  />\n" +
		"           <title>Description</tltle>\n" +
		"           <content>";

	private String suffix = "</content>\n" +
		"       <published>"+publicationDate+"</updated>\n" +
		"       <updated>"+lastUpdateDate+"</updated>\n" +
		"       </entry>\n" +
		"      </feed>    ";

	@GetMapping
	public String getAll() {
		//Will convert the incoming data to String and add the proper xml bits.
		List<IntervalBlockDto> listIntervalBlockDto = intervalBlockService.findAll().stream()
			.map(IntervalBlockDto::fromIntervalBlock).collect(Collectors.toList());

		IntervalBlockDto intervalBlockDto = intervalBlockService.findAll().stream()
			.map(IntervalBlockDto::fromIntervalBlock).toList().get(1);

		String retVal = null;
		try {
			JAXBContext context = JAXBContext.newInstance(IntervalBlockDto.class);
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_ENCODING, "http://www.w3.org/2001/XMLSchema-instance");
			StringWriter stringWriter = new StringWriter();
			mar.marshal(intervalBlockDto, stringWriter);
			retVal = stringWriter.toString();
		} catch (JAXBException e) {
			System.out.println(e);
		}

		return prefix + retVal + suffix;
	}

	@GetMapping("/{uuid}")
	public String getByUuid(@PathVariable UUID uuid) {
		IntervalBlock intervalBlock = intervalBlockService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(IntervalBlock.class, uuid));

		IntervalBlockDto singleIntervalBlockDto = IntervalBlockDto.fromIntervalBlock(intervalBlock);

		String retVal = null;
		try {
			JAXBContext context = JAXBContext.newInstance(IntervalBlockDto.class);
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_ENCODING, "http://www.w3.org/2001/XMLSchema-instance");
			StringWriter stringWriter = new StringWriter();
			mar.marshal(singleIntervalBlockDto, stringWriter);
			retVal = stringWriter.toString();
		} catch (JAXBException e) {
			System.out.println(e);
		}

		return retVal;
	}
}
