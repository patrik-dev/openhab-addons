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
import com.google.gson.JsonSyntaxException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.*;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.type.ChannelKind;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.timetree.internal.config.TimeTreeConfiguration;
import org.openhab.binding.timetree.internal.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import static org.openhab.binding.timetree.internal.TimeTreeBindingConstants.*;

/**
 * The {@link TimeTreeCalendarHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Patrik Schlinker - Initial contribution
 */
@NonNullByDefault
public class TimeTreeCalendarHandler extends BaseThingHandler {

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_CALENDAR);
    private final Logger logger = LoggerFactory.getLogger(TimeTreeCalendarHandler.class);

    private static final int DEFAULT_REFRESH_PERIOD = 15;

    private final Gson gson;

    private @Nullable ScheduledFuture<?> refreshJob;

    private @Nullable List<CalendarDataEvent> eventDatas;

    private @Nullable TimeTreeBridgeHandler bridgeHandler;

    public TimeTreeCalendarHandler(Thing thing) {
        super(thing);
        gson = new Gson();
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Time Tree Calendar handler for thing {}", getThing().getUID());
        Bridge bridge = getBridge();
        if( bridge == null ) {
            initializeThingHandler(null, null);
        } else {
            initializeThingHandler(bridge.getHandler(), bridge.getStatus());
        }
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        logger.debug("bridgeStatusChanged {}", bridgeStatusInfo);
        Bridge bridge = getBridge();
        if (bridge == null) {
            initializeThingHandler(null, bridgeStatusInfo.getStatus());
        } else {
            initializeThingHandler(bridge.getHandler(), bridgeStatusInfo.getStatus());
        }
    }

    private void initializeThingHandler(@Nullable ThingHandler bridgeHandler, @Nullable ThingStatus bridgeStatus) {
        logger.debug("initializeThingHandler {}", getThing().getUID());
        if (bridgeHandler != null && bridgeStatus != null) {
            this.bridgeHandler = (TimeTreeBridgeHandler) bridgeHandler;
            //TimeTreeConfiguration config = getConfigAs(TimeTreeConfiguration.class);
            updateStatus(ThingStatus.ONLINE);
            startAutomaticRefresh();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        }
    }

    private void startAutomaticRefresh() {
        ScheduledFuture<?> job = refreshJob;
        if (job == null || job.isCancelled()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        // Request new weather data to the Weather Underground service
                        updateUpcommingEvents();

                        // Update all channels from the updated weather data
                        for (Channel channel : getThing().getChannels()) {
                            updateChannels(channel.getUID().getId());
                        }
                    } catch (Exception e) {
                        logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                    }
                }
            };
            TimeTreeConfiguration config = getConfigAs(TimeTreeConfiguration.class);
            int period = (config.refresh != null) ? config.refresh.intValue() : DEFAULT_REFRESH_PERIOD;
            refreshJob = scheduler.scheduleWithFixedDelay(runnable, 0, period, TimeUnit.MINUTES);
        }
    }

    private boolean updateUpcommingEvents(){

        return false;
    }

    private void updateChannels(String channelId) {
        if(isLinked(channelId)){

        }
    }


    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }
}
