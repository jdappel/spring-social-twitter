/*
 * Copyright 2011 the original author or authors.
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

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.social.test.client.RequestMatchers.*;
import static org.springframework.social.test.client.ResponseCreators.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.social.ApiException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.ProviderServerErrorException;
import org.springframework.social.twitter.api.RateLimitException;

public class ApiErrorTest extends AbstractTwitterApiTest {

	@Test(expected = NotAuthorizedException.class)
	public void badOrMissingAccessToken() {
		mockServer.expect(requestTo("https://api.twitter.com/1/statuses/update.json"))
			.andExpect(method(POST))
			.andExpect(body("status=Some+message"))
			.andRespond(withResponse("", responseHeaders, HttpStatus.UNAUTHORIZED, ""));
		twitter.timelineOperations().updateStatus("Some message");		
	}
	
	@Test(expected = RateLimitException.class)
	public void enhanceYourCalm() {
		mockServer.expect(requestTo("https://search.twitter.com/search.json?q=%23spring&rpp=50&page=1"))
			.andExpect(method(GET))
			.andRespond(withResponse("{\"error\":\"You have been rate limited. Enhance your calm.\"}", responseHeaders, HttpStatus.valueOf(420), ""));		
		twitter.searchOperations().search("#spring");
	}

	@Test(expected = ProviderServerErrorException.class)
	public void twitterIsBroken() {
		try {
			mockServer.expect(requestTo("https://api.twitter.com/1/statuses/home_timeline.json"))
				.andExpect(method(GET))
				.andRespond(withResponse("Non-JSON body", responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR, ""));
			twitter.timelineOperations().getHomeTimeline();
		} catch (ProviderServerErrorException e) {
			assertEquals("Something is broken at Twitter. Please see http://dev.twitter.com/pages/support to report the issue.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected = ProviderServerErrorException.class)
	public void twitterIsDownOrBeingUpgraded() {
		try {
			mockServer.expect(requestTo("https://api.twitter.com/1/statuses/home_timeline.json"))
				.andExpect(method(GET))
				.andRespond(withResponse("Non-JSON body", responseHeaders, HttpStatus.BAD_GATEWAY, ""));
			twitter.timelineOperations().getHomeTimeline();
		} catch (ProviderServerErrorException e) {
			assertEquals("Twitter is down or is being upgraded.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected = ProviderServerErrorException.class)
	public void twitterIsOverloaded() {
		try {
			mockServer.expect(requestTo("https://api.twitter.com/1/statuses/home_timeline.json"))
				.andExpect(method(GET))
				.andRespond(withResponse("Non-JSON body", responseHeaders, HttpStatus.SERVICE_UNAVAILABLE, ""));
			twitter.timelineOperations().getHomeTimeline();
		} catch (ProviderServerErrorException e) {
			assertEquals("Twitter is overloaded with requests. Try again later.", e.getMessage());
			throw e;
		}
	}

	@Test(expected = ApiException.class)
	public void resourceNotFound() {
		try {
			mockServer.expect(requestTo("https://api.twitter.com/1/statuses/show/9876543210.json"))
				.andExpect(method(GET))
				.andRespond(withResponse("{\"error\":\"No status found with that ID.\", \"request\":\"/1/statuses/show/98765432109876.json\"}", responseHeaders, HttpStatus.NOT_FOUND, ""));
			twitter.timelineOperations().getStatus(9876543210L);
		} catch (ApiException e) {
			assertEquals("Resource not found: No status found with that ID.; Path: /1/statuses/show/98765432109876.json", e.getMessage());
			throw e;
		}
	}

	@Test(expected = ApiException.class)
	public void nonJSONErrorResponse() {
		try { 
			mockServer.expect(requestTo("https://api.twitter.com/1/statuses/home_timeline.json"))
				.andExpect(method(GET))
				.andRespond(withResponse("<h1>HTML response</h1>", responseHeaders, HttpStatus.BAD_REQUEST, ""));
			twitter.timelineOperations().getHomeTimeline();
		} catch (ApiException e) {
			assertEquals("Error consuming Twitter REST API", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected = ApiException.class)
	@Ignore("TODO: Need to handle cases where there isn't an error, but the body is unparseable.")
	public void unparseableSuccessResponse() {
		mockServer.expect(requestTo("https://api.twitter.com/1/statuses/home_timeline.json"))
			.andExpect(method(GET))
			.andRespond(withResponse("Unparseable {text}", responseHeaders, HttpStatus.OK, ""));
		twitter.timelineOperations().getHomeTimeline();		
	}
	
}
