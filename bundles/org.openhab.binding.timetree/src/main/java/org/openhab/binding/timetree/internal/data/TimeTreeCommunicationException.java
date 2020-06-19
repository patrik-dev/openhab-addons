/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.timetree.internal.data;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.timetree.internal.handler.TimeTreeCalendarHandler;

/**
 *
 * @author Patrik Schlinker - Initial contribution
 */
@NonNullByDefault
public class TimeTreeCommunicationException extends RuntimeException{

    TimeTreeCommunicationException() {
        super();
    }

    TimeTreeCommunicationException(String message){
        super(message);
    }

    TimeTreeCommunicationException(Throwable cause) {
        super(cause);
    }

    TimeTreeCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
