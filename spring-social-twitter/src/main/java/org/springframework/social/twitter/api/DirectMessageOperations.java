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
package org.springframework.social.twitter.api;

import java.util.List;

import org.springframework.social.ApiException;
import org.springframework.social.NotAuthorizedException;


/**
 * Interface defining the Twitter operations for working with direct messages.
 * @author Craig Walls
 */
public interface DirectMessageOperations {

	/**
	 * Retrieve the 20 most recently received direct messages for the authenticating user.
	 * @return a collection of {@link DirectMessage} with the authenticating user as the recipient.
	 * @throws NotAuthorizedException if TwitterTemplate was not created with OAuth credentials.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 */
	List<DirectMessage> getDirectMessagesReceived();

	/**
	 * Retrieve the 20 most recently sent direct messages for the authenticating
	 * user.
	 * 
	 * @return a collection of {@link DirectMessage} with the authenticating user as the sender.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws NotAuthorizedException if TwitterTemplate was not created with OAuth credentials.
	 */
	List<DirectMessage> getDirectMessagesSent();

	/**
	 * Sends a direct message to another Twitter user. The recipient of the
	 * message must follow the authenticated user in order for the message to be
	 * delivered. If the recipient is not following the authenticated user, an
	 * {@link InvalidMessageRecipientException} will be thrown.
	 * 
	 * @param toScreenName the screen name of the recipient of the messages.
	 * @param text the message text.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws InvalidMessageRecipientException if the recipient is not following the authenticating user.
	 * @throws DuplicateTweetException if the message duplicates a previously sent message.
	 * @throws MessageLengthException if the message length exceeds Twitter's 140 character limit.
	 * @throws NotAuthorizedException if TwitterTemplate was not created with OAuth credentials.
	 */
	void sendDirectMessage(String toScreenName, String text);

	/**
	 * Sends a direct message to another Twitter user.
	 * The recipient of the message must follow the authenticated user in order
	 * for the message to be delivered. If the recipient is not following the
	 * authenticated user, an {@link InvalidMessageRecipientException} will be thrown.
	 * @param toUserId the Twitter user ID of the recipient of the messages.
	 * @param text the message text.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws InvalidMessageRecipientException if the recipient is not following the authenticating user.
	 * @throws DuplicateTweetException if the message duplicates a previously sent message.
	 * @throws MessageLengthException if the message length exceeds Twitter's 140 character limit.
	 * @throws NotAuthorizedException if TwitterTemplate was not created with OAuth credentials.
	 */
	void sendDirectMessage(long toUserId, String text);
	
	/**
	 * Deletes a direct message for the authenticated user.
	 * @param messageId the ID of the message to be removed.
	 * @throws ApiException if there is an error while communicating with Twitter.
	 * @throws NotAuthorizedException if TwitterTemplate was not created with OAuth credentials.
	 */
	void deleteDirectMessage(long messageId);
}
