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

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @author Patrik Schlinker - Initial contribution
 */
public enum Endpoints {
    USER("/user"),
    CALENDARS("/calendars"),
    EVENTS("/calendars"),
    DUMMY("DUMMY");

    private String url;

    Endpoints( String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static Endpoints getEndpoint(String input) {
        return Arrays.asList(Endpoints.values()).stream().filter(url -> StringUtils.equalsIgnoreCase(input, url.getUrl())).findAny().orElse(DUMMY);
    }
}
