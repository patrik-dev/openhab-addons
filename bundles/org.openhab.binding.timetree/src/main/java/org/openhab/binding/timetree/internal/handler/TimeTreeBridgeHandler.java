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
package org.openhab.binding.timetree.internal.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.*;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.timetree.internal.config.TimeTreeConfiguration;
import org.openhab.binding.timetree.internal.data.CalendarAccess;
import org.openhab.binding.timetree.internal.data.CalendarData;
import org.openhab.binding.timetree.internal.data.Endpoints;
import org.openhab.binding.timetree.internal.data.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.openhab.binding.timetree.internal.TimeTreeBindingConstants.*;

/**
 * The {@link TimeTreeBridgeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Patrik Schlinker - Initial contribution
 */
@NonNullByDefault
public class TimeTreeBridgeHandler extends BaseBridgeHandler {

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_ACCOUNT);
    private final Logger logger = LoggerFactory.getLogger(TimeTreeBridgeHandler.class);

    private static final String TIMETREEAPI_URL = "https://timetreeapis.com";

    private Gson gson;
    private String accessToken = "";
    private int refresh = 15;

    public static final int FETCH_TIMEOUT_MS = 30000;

    private @Nullable ScheduledFuture<?> refreshJob;

    public static final Properties HTTP_HEADERS;
    static {
        HTTP_HEADERS = new Properties();
        HTTP_HEADERS.put("Content-Type", "application/json;charset=UTF-8");
        HTTP_HEADERS.put("User-Agent", "openhab-timetree-api/2.0");
    }

    public TimeTreeBridgeHandler(Bridge bridge) {
        super(bridge);
        gson = new Gson();
    }

    @Override
    public void initialize() {
        logger.debug("Initializing TimeTree Bridge Handler");
        Configuration config = getThing().getConfiguration();

        Object personalAccessToken = config.get(PERSONALACCESSTOKEN);
        if(personalAccessToken == null || !(personalAccessToken instanceof String) || StringUtils.isEmpty((String)personalAccessToken)) {
            logger.debug("Setting thing '{}' to OFFLINE: Parameter 'personalAccessToken' must be configured.", getThing().getUID());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.conf-error-missing-personalAccessToken");
        } else {
            Object refreshTime = config.get(REFRESH);
            if(refreshTime != null && NumberUtils.isNumber(refreshTime.toString())) {
                refresh = NumberUtils.toInt(refreshTime.toString());
            }
            accessToken = ((String)personalAccessToken).trim();
            updateStatus(ThingStatus.UNKNOWN);
            startRefreshJob();
        }
    }

    private void startRefreshJob() {
        if(refreshJob == null || refreshJob.isCancelled()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    UserData result = new UserData();
                    boolean resultOk = true;
                    StringBuilder builder = new StringBuilder();
                    builder.append(TIMETREEAPI_URL);
                    builder.append(Endpoints.USER.getUrl());
                    String resultString = "";
                    try{
                        resultString = HttpUtil.executeUrl("GET", builder.toString(), setHeaders(), null, "application/json", FETCH_TIMEOUT_MS);
                    } catch(IOException e) {
                        logger.debug("Error attempting to find calendar events", e);
                        resultOk = false;
                    }
                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(resultString).getAsJsonObject();

                    result = gson.fromJson(jsonObject.get("data").toString(), UserData.class);

                    if(resultOk) {
                        updateStatus(ThingStatus.ONLINE);
                    } else {
                        logger.debug("Setting thing '{}' to OFFLINE: Error while getting Clendars", getThing().getUID());
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR);
                    }
                }
            };
            refreshJob = scheduler.schedule(runnable, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void dispose() {
        logger.debug("Disposing Time Tree Bridge Handler");

        if( refreshJob != null && !refreshJob.isCancelled()) {
            refreshJob.cancel(true);
            refreshJob = null;
        }
    }

    private Properties setHeaders() {
        Properties headers = new Properties();
        headers.putAll(HTTP_HEADERS);
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // not needed
    }
}
