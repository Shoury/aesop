/*
 * Copyright 2012-2015, the original author or authors.
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
package org.aesop.runtime.producer.eventprocessor.impl;

import org.aesop.runtime.producer.eventlistener.OpenReplicationListener;
import org.aesop.runtime.producer.eventprocessor.BinLogEventProcessor;
import org.trpr.platform.core.impl.logging.LogFactory;
import org.trpr.platform.core.spi.logging.Logger;

import com.google.code.or.binlog.BinlogEventV4;
/**
 * The <code>CommitEventProcessor</code> processes XIDEvent from source. This event is invoked when a particular transaction is committed.
 * This event triggers end of particular transaction.
 * @author Shoury B
 * @version 1.0, 07 Mar 2014
 */
public class CommitEventProcessor implements BinLogEventProcessor{
	/** Logger for this class*/
	private static final Logger LOGGER = LogFactory.getLogger(CommitEventProcessor.class);

	/**
	 * @see org.aesop.runtime.producer.eventprocessor.BinLogEventProcessor#process(com.google.code.or.binlog.BinlogEventV4, org.aesop.runtime.producer.eventlistener.OpenReplicationListener)
	 */
	@Override
	public void process(BinlogEventV4 event, OpenReplicationListener listener) throws Exception {
		if ( !listener.getMysqlTransactionManager().isBeginTxnSeen()){
			LOGGER.warn("Skipping event (" + event
					+ ") as this is before the start of first transaction");
			return;
		}
		LOGGER.debug("COMMIT sql: COMMIT");
		listener.getMysqlTransactionManager().endXtion(event.getHeader().getTimestamp());
	}
}
