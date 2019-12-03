package com.logic.util;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    //返回项目图片的跟路径
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "D:/projectdev/image/";
        }else{
            //
            //注意将此处设置为有操作权限的文件夹，mac下的/home文件夹可能就没有权限
            //basePath = "/home/work/pzt/image/";  //(没有权限，导致程序无法创建各级目录，会报错)
            basePath = "/Users/pzt/Documents/java_project_image/";
        }
        basePath = basePath.replace("/",separator);
        return basePath;
    }

    //返回项目图片相对子路径，将图片存在各自的商铺下面
    public static String getShopImagePath(long shopId){
        String imagePath = "upload/item/shop/" + shopId + '/';
        return imagePath.replace("/",separator);
    }

}
