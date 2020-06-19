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

import com.google.gson.annotations.SerializedName;

/**
 * @author Patrik Schlinker - Initial contribution
 */
public class CalendarDataEventAttributes {
    String title;
    String description;
    @SerializedName("start_at")
    String startAt;
    @SerializedName("start_timezone")
    String startTimeZone;
    @SerializedName("end_at")
    String endAt;
    @SerializedName("end_timezone")
    String endTimeZone;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getStartTimeZone() {
        return startTimeZone;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getEndTimeZone() {
        return endTimeZone;
    }
}
