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
package org.openhab.binding.timetree.internal.factory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.timetree.internal.discovery.TimeTreeDiscoveryService;
import org.openhab.binding.timetree.internal.handler.TimeTreeBridgeHandler;
import org.openhab.binding.timetree.internal.handler.TimeTreeCalendarHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;

import static org.openhab.binding.timetree.internal.TimeTreeBindingConstants.THING_TYPE_ACCOUNT;
import static org.openhab.binding.timetree.internal.TimeTreeBindingConstants.THING_TYPE_CALENDAR;

/**
 * The {@link TimeTreeHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Patrik Schlinker - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.timetree", service = ThingHandlerFactory.class)
public class TimeTreeHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .unmodifiableSet(Stream.concat(TimeTreeBridgeHandler.SUPPORTED_THING_TYPES.stream(),
                    TimeTreeCalendarHandler.SUPPORTED_THING_TYPES.stream()).collect(Collectors.toSet()));
    private final Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if(THING_TYPE_ACCOUNT.equals(thingTypeUID)) {
            TimeTreeBridgeHandler apiHandler = new TimeTreeBridgeHandler((Bridge) thing);
            registerDiscoveryService(apiHandler);
            return apiHandler;
        } else if(THING_TYPE_CALENDAR.equals(thingTypeUID)) {
            return new TimeTreeCalendarHandler(thing);
        }
        return null;
    }

    private void registerDiscoveryService(TimeTreeBridgeHandler apiHandler) {
        TimeTreeDiscoveryService discoveryService = new TimeTreeDiscoveryService(apiHandler);
        discoveryServiceRegs.put(apiHandler.getThing().getUID(), bundleContext
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }

    @Override
    protected void removeHandler(ThingHandler thingHandler) {
        if(thingHandler instanceof TimeTreeBridgeHandler) {
            unregisterDiscoveryService(thingHandler.getThing().getUID());
        }
    }

    private void unregisterDiscoveryService(ThingUID bridgeUID) {
        ServiceRegistration<?> serviceReg = discoveryServiceRegs.remove(bridgeUID);
        if (serviceReg != null) {
            serviceReg.unregister();
        }
    }
}
