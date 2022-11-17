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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.greenbuttonalliance.gbaresourceserver.usage.model.IntervalBlock;
import org.greenbuttonalliance.gbaresourceserver.usage.web.controller.exception.EntityNotFoundByIdException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
//
//	private static final SimpleDateFormat publicationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//	private static final SimpleDateFormat lastUpdateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//
//	private String parentPrefix = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//		"           <feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//		"               <id>urn:uuid:046b4788-f971-4662-b177-6d94832dd403</id>\n" +
//		"               <title>Green Button Usage Feed</title>\n" +
//		"               <updated>" + lastUpdateDate +"</updated>\n";
//
//	private String parentSuffix = "      </feed>    ";
//
//	protected String getPrefix() {
//		IntervalBlock intervalBlock = intervalBlockService.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundByIdException(IntervalBlock.class, uuid));
//		IntervalBlockDto singleIntervalBlockDto = IntervalBlockDto.fromIntervalBlock(intervalBlock);
//
//		String prefix = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//			"           <feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//			"               <id>"+uuid+"</id>\n" +
//			"               <title>Green Button Usage Feed</title>\n" +
//			"               <updated>" + lastUpdateDate +"</updated>\n" +
//			"       <entry xmlns:espi=\"http://naesb.org/espi\" xmlns=\"http://www.w3.org/2005/Atom\">\n" +
//			"           <id>"+ uuid +"</id> \n" +
//			"           <link rel=\"self\" href=\""+ singleIntervalBlockDto.getSelfLinkHref() +"\" type=\"espi-entry/Subscription\" />\n" +
//			"           <link rel=\"up\" href=\""+ singleIntervalBlockDto.getUpLinkHref() +"\" type=\"espi-feed/UsagePoint\" />\n" +
//			"          <link rel=\"related\" href=\"" + singleIntervalBlockDto.getUpLinkRel() +"\" type=\"espi-entry/ElectricPowerQualitySummary\"  />\n" +
//			"           <title>"+ singleIntervalBlockDto.getDescription()+"</tltle>\n" +
//			"           <content>";
//	}

}
