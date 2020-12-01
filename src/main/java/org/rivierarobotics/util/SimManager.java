/*
 * This file is part of Swerve2020, licensed under the GNU General Public License (GPLv3).
 *
 * Copyright (c) Riviera Robotics <https://github.com/Team5818>
 * Copyright (c) contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rivierarobotics.util;

import edu.wpi.first.hal.HALValue;
import edu.wpi.first.hal.SimBoolean;
import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.SimValue;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimManager {
    private static final Map<NamedDevice, List<SimValue>> devices = new LinkedHashMap<>();

    private SimManager() {
    }

    public static NamedDevice addDevice(String name) {
        NamedDevice device = new NamedDevice(name);
        devices.put(device, new LinkedList<>());
        return device;
    }

    public static NamedDevice getDevice(String name) {
        for (NamedDevice device : devices.keySet()) {
            if (device.name.equals(name)) {
                return device;
            }
        }
        return addDevice(name);
    }

    public static class NamedDevice {
        protected final Map<String, Object> values;
        protected final SimDevice device;
        protected final String name;

        public NamedDevice(String name) {
            this.values = new LinkedHashMap<>();
            this.device = SimDevice.create(name);
            this.name = name;
        }

        public <K> SimValue addValue(String key, K value) {
            //TODO add enum option
            if (value instanceof Double) {
                values.put(key, device.createDouble(key, false, 0.0D));
            } else if (value instanceof Boolean) {
                values.put(key, device.createBoolean(key, false, false));
            }
            return null;
        }

        public <K> void setValue(String key, K value) {
            if (!values.containsKey(key)) {
                addValue(key, value);
            }
            if (value instanceof Double) {
                ((SimDouble) values.get(key)).set((Double) value);
            } else if (value instanceof Boolean) {
                ((SimBoolean) values.get(key)).set((Boolean) value);
            }
        }

        public HALValue getValue(String key) {
            if (values.containsKey(key)) {
                return ((SimValue) values.get(key)).getValue();
            }
            return HALValue.makeDouble(0);
        }
    }
}
