package com.philkes.youtubify.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Nirmal Dhara on 12-07-2015.
 */
public class Util {
    public static String getProperty(String file, String key, Context context) throws IOException {
        Properties properties=new Properties();
        AssetManager assetManager=context.getAssets();
        InputStream inputStream=assetManager.open(file);
        properties.load(inputStream);
        return properties.getProperty(key);

    }
}