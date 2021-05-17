package bme.pong.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

public class IniParser implements IConfigMgr {
    private final TreeMap<String, String> _content; // TreeMap is ordered

    public IniParser() {
        _content = new TreeMap<>();
    }

    public IniParser(String ini) {
        _content = new TreeMap<>();
        String[] iniLines = ini.split("\n");
        System.out.println("ini lines count: " + iniLines.length);
        for (String line : iniLines) {
            // ltrim() emulation
            if (line.replaceAll("^\\s+", "").charAt(0) == '[') {
                continue;
            }
            this.parseLine(line);
        }
    }

    public static IniParser fromFile(String filename) throws IOException {
        String content  = Files.readString(Path.of(filename));
        return new IniParser(content);
    }

    public String getKey(String key) throws RuntimeException {
        if (!_content.containsKey(key)) {
            throw new RuntimeException("Key '" + key + "' not found");
        }

        return _content.get(key);
    }

    public String getKey(String key, String defaultValue) {
        if (!_content.containsKey(key)) {
            return defaultValue;
        }

        return _content.get(key);
    }

    public void setKey(String key, String value) {
        _content.put(key, value);
    }

    public String save() {
        StringBuilder builder = new StringBuilder();
        for (String key : _content.keySet()) {
            builder.append(key).append(" = ").append(_content.get(key)).append("\n");
        }

        return builder.toString();
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.print(this.save());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) throws RuntimeException {
        String[] kvp = line.split("=");
        if (kvp.length != 2) {
            throw new RuntimeException("Error while parsing line '" + line + "': missing '=' sign ");
        }
        _content.put(kvp[0].trim(), kvp[1].trim());
    }
}
