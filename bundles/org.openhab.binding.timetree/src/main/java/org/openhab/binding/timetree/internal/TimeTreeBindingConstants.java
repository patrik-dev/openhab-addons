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
package org.openhab.binding.timetree.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@link TimeTreeBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Patrik Schlinker - Initial contribution
 */
@NonNullByDefault
public class TimeTreeBindingConstants {

    private static final String BINDING_ID = "timetree";

    // bridge
    public static final ThingTypeUID THING_TYPE_ACCOUNT = new ThingTypeUID(BINDING_ID, "account");

    // calendar
    public static final ThingTypeUID THING_TYPE_CALENDAR = new ThingTypeUID(BINDING_ID, "calendar");

    public static final Set<ThingTypeUID> SUPPORTED_DEVICE_THING_TYPES_UIDS = new HashSet<>(Arrays.asList(THING_TYPE_CALENDAR));

    // List of all Channel ids
    public static final String CHANNEL_LAST_UPDATED = "lastUpdated";
    public static final String CHANNEL_UPDATES = "updates";
    public static final String CHANNEL_CALENDARNAME = "calendarName";

    public static final String PERSONALACCESSTOKEN = "personalAccessToken";
    public static final String REFRESH = "refresh";

}
