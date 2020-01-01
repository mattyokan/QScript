package org.quadrex.script.core.model;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.quadrex.script.api.logger.QLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;

public class ScriptAttributes {

    private File file;
    private String name;
    private String main;
    private String version;

    public ScriptAttributes(File file, String name, String main, String version) {
        this.file = file;
        this.name = name;
        this.main = main;
        this.version = version;
    }

    public static Optional<ScriptAttributes> fromJsonFile(File file) {
        try {
            InputStream is = new FileInputStream(file);
            String jsonText = IOUtils.toString(is, "UTF-8");
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(jsonText);

            String name = json.get("name").toString();
            String main = json.get("main").toString();
            String version = json.get("version").toString();

            if(name == null || main == null || version == null) {
                QLog.log(Level.WARNING, () ->
                        "====[ QScript Loading Error! ]====\n" +
                        "Error loading the script in " + file.getPath() + "\n" +
                        "Name:" + name + "\n" +
                        "Main QScript:" + main + "\n" +
                        "Version:" + version + "\n" +
                        "=================================="
                );
                return Optional.empty();
            }
            return Optional.of(new ScriptAttributes(file, name, main, version));
        } catch(Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getMain() {
        return main;
    }

    public String getVersion() {
        return version;
    }
}
