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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author Patrik Schlinker - Initial contribution
 */
public class CalendarAccess {

    private final Logger logger = LoggerFactory.getLogger(CalendarAccess.class);
    private static final String TIMETREEAPI_URL = "https://timetreeapis.com";
    private final String accessToken;
    private Gson gson = new Gson();

    public static final Properties HTTP_HEADERS;
    static {
        HTTP_HEADERS = new Properties();
        HTTP_HEADERS.put("Content-Type", "application/json;charset=UTF-8");
        HTTP_HEADERS.put("User-Agent", "openhab-ecobee-api/2.0");
    }

    public CalendarAccess(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<CalendarData> getCalendars() {
        List<CalendarData> resultList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(TIMETREEAPI_URL);
        builder.append(Endpoints.CALENDARS.getUrl());
        String resultString = "";
        try {
            resultString = HttpUtil.executeUrl("GET", builder.toString(), setHeaders(), null, "application/json", 2000);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(resultString).getAsJsonObject();

            resultList.addAll(Arrays.asList( gson.fromJson(jsonObject.get("data").toString(), CalendarData[].class)));
        } catch(IOException e) {
            logger.debug("Error attempting to find calendar events", e);
        }
        return resultList;
    }

    public List<CalendarDataEvent> getCalendarEvents(String calendarId) {
        List<CalendarDataEvent> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(calendarId)){
            logger.warn("No calendarId provided");
            return resultList;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(TIMETREEAPI_URL);
        builder.append(Endpoints.CALENDARS.getUrl());
        builder.append("/");
        builder.append(calendarId);
        builder.append("/upcoming_events?days=7");
        try {
            String resultString = HttpUtil.executeUrl("GET", builder.toString(), setHeaders(), null, "application/json", 2000);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(resultString).getAsJsonObject();

            resultList.addAll(Arrays.asList( gson.fromJson(jsonObject.get("data").toString(), CalendarDataEvent[].class)));
        } catch(IOException e) {
            logger.debug("Error attempting to find calendar events", e);
        }
        return resultList;
    }

    private Properties setHeaders() {
        Properties headers = new Properties();
        headers.putAll(HTTP_HEADERS);
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
