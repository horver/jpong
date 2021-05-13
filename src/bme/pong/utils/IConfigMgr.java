package bme.pong.utils;

public interface IConfigMgr {
    public String getKey(String key);
    public String getKey(String key, String defaultValue);
    public void setKey(String key, String value);
    public String save();
    public void saveToFile(String filename);
}
