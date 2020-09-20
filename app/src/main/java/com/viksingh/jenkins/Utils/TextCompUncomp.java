package com.viksingh.jenkins.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TextCompUncomp {

    public static String compress(String paramString1, String paramString2) {
        if (paramString1 == null || paramString1.length() == 0)
            return paramString1;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(paramString1.getBytes(paramString2));
            gZIPOutputStream.close();
            return URLEncoder.encode(byteArrayOutputStream.toString("ISO-8859-1"), "UTF-8");
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public static String decompress(String paramString1, String paramString2) {
        if (paramString1 == null || paramString1.length() == 0)
            return paramString1;
        try {
            String str = URLDecoder.decode(paramString1, "UTF-8");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] arrayOfByte = new byte[256];
            while (true) {
                int i = gZIPInputStream.read(arrayOfByte);
                if (i >= 0) {
                    byteArrayOutputStream.write(arrayOfByte, 0, i);
                    continue;
                }
                return byteArrayOutputStream.toString(paramString2);
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }
}
