package com.example.Util;

import java.io.File;

/**
 * Created by Administrator on 2017/3/9.
 */

public class FileTypeManager {
    public static FileTypeManager mInstance;

    public static FileTypeManager getmInstance()
    {
        return mInstance;
    }
    public FileTypeManager()
    {
        if(mInstance==null)
        {
            mInstance=new FileTypeManager();
        }
    }

    /**
     * 从SD卡中获取资源图片的路径
     */
    private void getImagePathFromSD(String p) {

        try {
            File mFile = new File(p);
            File[] files = mFile.listFiles();

        /* 将所有文件存入ArrayList中 */
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    getImagePathFromSD(file.getPath());
                }
                switch(checkFormat(file.getPath()))
                {
                    case "picture":

                        break;
                    case "video":

                        break;
                    case "music":

                        break;
                    case "ppt":

                        break;
                    default:

                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否相应的图片格式
     */
    private String checkFormat(String fName) {
        String Format;

        /* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        /* 按扩展名的类型决定MimeType */
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            Format = "picture";
        }
        else if (end.equals("ppt") || end.equals("pptx")) {
            Format = "ppt";
        }
        else if (end.equals("mp4") || end.equals("3gp")) {
            Format = "video";
        }
        else if (end.equals("mp3") || end.equals("wav") || end.equals("3gp")) {
            Format="music";
        }
        else {
            Format = "null";
        }
        return Format;
    }

}
