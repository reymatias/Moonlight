package helios.moonlight_android;

import java.util.HashMap;

public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String HELIOS_CONFIG = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String HELIOS_RX_TX = "0000fff3-0000-1000-8000-00805f9b34fb";

    static {
        attributes.put(HELIOS_CONFIG, "Helios R/W Service");
        attributes.put(HELIOS_RX_TX, "Helios R/W Characteristic");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
