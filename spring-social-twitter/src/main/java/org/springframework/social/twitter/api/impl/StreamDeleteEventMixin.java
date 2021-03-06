/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.twitter.api.impl;

import java.io.IOException;

import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.impl.StreamDeleteEventMixin.DeleteTweetEventDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = DeleteTweetEventDeserializer.class)
class StreamDeleteEventMixin {
	
	static final class DeleteTweetEventDeserializer extends JsonDeserializer<StreamDeleteEvent> {
		@Override
		public StreamDeleteEvent deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonNode deleteNode = jp.readValueAs(JsonNode.class).get("delete").get("status");
			return new StreamDeleteEvent(deleteNode.get("id").asLong(), deleteNode.get("user_id").asLong());
		}
	}
	
}
