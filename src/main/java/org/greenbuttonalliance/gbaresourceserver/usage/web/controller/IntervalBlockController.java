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
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.IdentifiedObjectDto;
import org.greenbuttonalliance.gbaresourceserver.usage.web.dto.IntervalBlockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
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

	@GetMapping
	public String getAll() {
		List<IntervalBlockDto> listIntervalBlockDto = intervalBlockService.findAll().stream()
			.map(IntervalBlockDto::fromIntervalBlock).toList();

		StringBuilder contentSB = new StringBuilder();

		for(IntervalBlockDto intervalBlockDto : listIntervalBlockDto) {
			String content = IdentifiedObjectDto.getContent(intervalBlockDto);
			contentSB.append(intervalBlockDto.addWrapper(content, true));
		}
		return IdentifiedObjectDto.addParentWrapper(contentSB.toString(), "IntervalBlock");
	}

	@GetMapping("/{uuid}")
	public String getByUuid(@PathVariable UUID uuid) {

		IntervalBlock intervalBlock = intervalBlockService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(IntervalBlock.class, uuid));
		IntervalBlockDto singleIntervalBlockDto = IntervalBlockDto.fromIntervalBlock(intervalBlock);

		String content = IdentifiedObjectDto.getContent(singleIntervalBlockDto);

		return singleIntervalBlockDto.addWrapper(content, false);
	}
}
