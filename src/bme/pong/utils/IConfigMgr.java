package bme.pong.utils;

public interface IConfigMgr {
    String getKey(String key);

    String getKey(String key, String defaultValue);

    void setKey(String key, String value);

    String save();

    void saveToFile(String filename);
}
