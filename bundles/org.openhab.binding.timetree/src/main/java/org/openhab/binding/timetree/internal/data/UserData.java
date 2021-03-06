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
public class UserData {
    String id;
    String type;
    UserDataAttributes attributes;

    public UserData() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public UserDataAttributes getAttributes() {
        return attributes;
    }
}
