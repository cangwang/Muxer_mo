package cangwang.com.base.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cangwang.com.base.bean.ReadItemBean;

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

    public static List<Map<String, Object>> getDir(String curPath){
        File f = new File(curPath);
        File[] file = f.listFiles();
        final List<Map<String, Object>> listItem = new ArrayList<>();

        if(!curPath.equals("/")){//如果不是根目录的话就在要显示的列表中加入此项
            Map map1=new HashMap();
            map1.put("name", "返回上一级目录");
            map1.put("image", android.R.drawable.ic_menu_revert);
            map1.put("path",f.getParent());
            map1.put("isDire", true);
            listItem.add(map1);
        }

        if(file != null){//必须判断 否则目录为空的时候会报错
            for(int i = 0; i < file.length; i++){
                Map map=new HashMap();
                map.put("name", file[i].getName());
                map.put("image", (file[i].isDirectory()) ? android.R.drawable.ic_menu_manage : android.R.drawable.ic_menu_info_details);
                map.put("path",file[i].getPath());
                map.put("isDire", file[i].isDirectory());
                listItem.add(map);
            }
        }
        return listItem;
    }

    public static List<ReadItemBean> getRecordFiles(String path){
        List<ReadItemBean> list = new ArrayList<>();
        File f = new File(path);
        File[] files = f.listFiles();// 列出所有文件
        if(files != null){
            int count = files.length;// 文件个数
            for (int i = 0; i < count; i++) {
                File file = files[i];
                if (file.getName().trim().toLowerCase().endsWith(".mp4")) {
                    list.add(new ReadItemBean(file.getPath(),
                            file.getName(),
                            getSpace(file.length()),
                            getCreateTime(file.getPath())));
                }
            }
        }
        return list;
    }

    public static String getSpace(long size){
        DecimalFormat df = new DecimalFormat("#.00");
        String transformSize = "未知";
        long calsize = size;
        int count =0;
        while(calsize >1024){
            calsize = calsize/1024;
            count++;
        }
        if(count ==0) {
            transformSize = Double.valueOf(df.format((double) size))+"B";
        }else if(count == 1){
            transformSize = Double.valueOf(df.format((double) size /1024))+"KB";
        }else if(count == 2){
            transformSize = Double.valueOf(df.format((double) size /1048576))+"MB";
        }else if(count == 3) {
            transformSize = Double.valueOf(df.format((double) size /1073741824)) + "GB";
        }
        return transformSize;
    }

    public static String getCreateTime(String filePath){
        String strTime = "未知";
        try {
            Process p = Runtime.getRuntime().exec("cmd /C dir "
                    + filePath
                    + "/tc" );
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = br.readLine()) != null){
                if(line.endsWith(".txt")){
                    strTime = line.substring(0,17);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strTime;
//        System.out.println("创建时间    " + strTime);
//        //输出：创建时间   2009-08-17  10:21
    }

    public static Bitmap getVideoThumbnail(String name,String filePath) {
        if(ImageLoader.getInstane().getImage(name) != null)
            return ImageLoader.getInstane().getImage(name);

        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if(bitmap != null)
            bitmap = resize(name,bitmap);
        return bitmap;
    }

    private static Bitmap resize(String name,Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.25f,0.25f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        ImageLoader.getInstane().addImage(name,resizeBmp);
        return resizeBmp;
    }

}
