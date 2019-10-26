package com.abby.jiaqing.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.misc.BASE64Decoder;

public class ImageUtil {
    public static File base64ToFile(String base64,String fileName) throws IOException {
        if(base64.contains("data:image")){
            base64=base64.substring(base64.indexOf(",")+1);
        }else{
            return null;
        }

        File file=new File(getImageFilePath()+File.separator+fileName);
        System.out.println(getImageFilePath()+File.separator+fileName);
        if(file.getParentFile()!=null&&!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()){
            boolean success=file.createNewFile();
            if(!success){
                System.out.println("create file "+fileName+" failed");
            }
        }
        BASE64Decoder decoder=new BASE64Decoder();
        byte[] bytes=decoder.decodeBuffer(base64);
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
        return file;
    }

    public static String getImageFilePath(){
        return "F:"+File.separator+"tmp";
    }
}
