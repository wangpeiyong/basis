package org.wpy.security;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.InputStream;

/**
 * DESC   文件处理工具类
 *
 * @author wpy
 * @create 2017-07-31 上午11:55
 **/
public class FileUtils {

    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片.
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(File imageFile) {
        if (!imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }


    public static boolean isImage(InputStream is) {
        Image img = null;
        try {
            img = ImageIO.read(is);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }

}
