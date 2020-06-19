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
package org.openhab.binding.timetree.internal.config;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link TimeTreeConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Patrik Schlinker - Initial contribution
 */
@NonNullByDefault
public class TimeTreeConfiguration {
    public Integer refresh = 15;
    public String personalAccessToken = StringUtils.EMPTY;
}
