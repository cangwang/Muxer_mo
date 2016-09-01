package material.com.muxer.utils;

import java.io.File;
import java.util.Vector;

/**
 * Created by air on 16/7/7.
 */
public class ReadUtil {

    /**
     * 获取当前目录下所有的mp4文件
     * @param fileAbsolutePath
     * @return
     */
    public static Vector<String> GetVideoFileName(String fileAbsolutePath){
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for(int iFileLength = 0; iFileLength <subFile.length; iFileLength++){
            //判断是否为文件夹
            if(!subFile[iFileLength].isDirectory()){
                String filename = subFile[iFileLength].getName();
                if(filename.trim().toLowerCase().endsWith(".mp4")){
                    vecFile.add(filename);
                }
            }
        }

        return vecFile;
    }
}
