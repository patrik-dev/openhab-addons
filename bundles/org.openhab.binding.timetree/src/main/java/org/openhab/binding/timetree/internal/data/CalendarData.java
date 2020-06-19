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

/**
 * @author Patrik Schlinker - Initial contribution
 */
public class CalendarData {
    String id;
    String type;
    CalendarDataAttributes attributes;

    public CalendarData() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public CalendarDataAttributes getAttributes() {
        return attributes;
    }
}
