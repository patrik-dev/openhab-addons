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
package org.openhab.binding.timetree.internal.discovery;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.timetree.internal.data.CalendarAccess;
import org.openhab.binding.timetree.internal.data.CalendarData;
import org.openhab.binding.timetree.internal.handler.TimeTreeBridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.openhab.binding.timetree.internal.TimeTreeBindingConstants.THING_TYPE_ACCOUNT;
import static org.openhab.binding.timetree.internal.TimeTreeBindingConstants.THING_TYPE_CALENDAR;

/**
 * The {@link TimeTreeDiscoveryService} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Patrik Schlinker - Initial contribution
 */
public class TimeTreeDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(TimeTreeDiscoveryService.class);

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = new HashSet<>(Arrays.asList(THING_TYPE_ACCOUNT));

    private static final int DISCOVERY_TIMEOUT_SECONDS = 2;
    private static final int DISCOVERY_INTERVAL_SECONDS = 60;
    private @Nullable ScheduledFuture<?> discoveryJob;

    private final TimeTreeBridgeHandler apiHandler;

    public TimeTreeDiscoveryService(TimeTreeBridgeHandler apihandler) {
        super(SUPPORTED_THING_TYPES, DISCOVERY_TIMEOUT_SECONDS);
        this.apiHandler = apihandler;
    }

    @Override
    protected void startScan() {
        logger.debug("Start manual TimeTree Calendar discovery scan.");
        scanForNewCalendar();
    }

    @Override
    protected synchronized void stopScan() {
        logger.debug("Stop manual TimeTree Calendar discovery scan.");
        super.stopScan();
    }

    @Override
    protected void startBackgroundDiscovery() {
        if (discoveryJob == null || discoveryJob.isCancelled()) {
            logger.debug("Start TimeTree Calendar background discovery job at interval {} s.",
                    DISCOVERY_INTERVAL_SECONDS);
            discoveryJob = scheduler.scheduleWithFixedDelay(this::scanForNewCalendar, 0, DISCOVERY_INTERVAL_SECONDS,
                    TimeUnit.SECONDS);
        }
    }

    @Override
    protected void stopBackgroundDiscovery() {
        if (discoveryJob != null && !discoveryJob.isCancelled()) {
            logger.debug("Stop TimeTree Calendar background discovery job.");
            if (discoveryJob.cancel(true)) {
                discoveryJob = null;
            }
        }
    }

    private void scanForNewCalendar() {
        String token = apiHandler.getThing().getConfiguration().get("personalAccessToken").toString();
        CalendarAccess calendarAccess = new CalendarAccess(token);
        List<CalendarData> calendars = calendarAccess.getCalendars();
        for(CalendarData calendar : calendars) {
            thingDiscovered(DiscoveryResultBuilder
                    .create(new ThingUID(THING_TYPE_CALENDAR, calendar.getId()))
                    .withLabel(calendar.getAttributes().getName())
                    .withBridge(apiHandler.getThing().getUID())
                    .build());
        }
    }
}
