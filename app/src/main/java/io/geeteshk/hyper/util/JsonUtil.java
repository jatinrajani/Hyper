package io.geeteshk.hyper.util;

import android.os.Environment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Utility class to handle JSON
 */
public class JsonUtil {

    /**
     * Creates the .hyper file at the root of a project
     *
     * @param name        of the project
     * @param author      of the project
     * @param description of the project
     * @param keywords    about the project
     * @return true if successful
     */
    public static boolean createProjectFile(String name, String author, String description, String keywords) {
        try {
            JSONObject object = new JSONObject();
            object.put("name", name);
            object.put("author", author);
            object.put("description", description);
            object.put("keywords", keywords);

            OutputStream stream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + File.separator + "Hyper" + File.separator + name + File.separator + name + ".hyper"));
            stream.write(object.toString(4).getBytes());
            stream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Reads contents from project file
     *
     * @param name    of project
     * @return project file contents
     */
    private static String getProjectJSON(String name) {
        try {
            InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "Hyper" + File.separator + name + File.separator + name + ".hyper");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append(System.getProperty("line.separator"));
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Reads properties from project JSON
     *
     * @param name    of project
     * @param prop    property that needs to be read
     * @return value of property
     */
    public static String getProjectProperty(String name, String prop) {
        try {
            String json = getProjectJSON(name);
            JSONObject object = new JSONObject(json);
            return object.getString(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}