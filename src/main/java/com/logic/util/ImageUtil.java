package com.logic.util;

import com.logic.web.superadmin.AreaController;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.slf4j.Logger;

public class ImageUtil {
    private static Logger logger = LoggerFactory.getLogger(AreaController.class);
    //获取classpath的绝对路径
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    //处理用户传入的文件（文件流），缩略图，门面照及商品小图
    public static String generateThumbnail(InputStream thumbnail,String fileName,String targetAddr){
        String realFileName = getRandomFileName();       //随机生成不重复的文件名
        String extension = getFileExtension(fileName);  //获取用户传入文件的后缀名
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try{
            Thumbnails.of(thumbnail)
                    .size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "watermark.png")),0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);

        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，如/home/work/xiangze/xx.jpg，那么home,work,xiangze这三个文件夹都得自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            System.out.println(dirPath + "文件夹不存在，将自动创建");
            dirPath.mkdirs();  //注意需要文件夹有操作权限，之前/home文件夹操作就没有权限，无法创建文件
        }
    }

    /**
     * 获取输入文件流的拓展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")); //substring方法返回从index位置到最后的字符串
    }


    /**
     * 生成随机不重复文件名 ，当前年月日十分秒+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        //获取随机五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return  nowTimeStr + rannum;
    }

    /**
     * 删除storePath对应的文件 或者 删除storePath对应的目录及该目录底下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if(fileOrPath.isDirectory()){
            File[] files = fileOrPath.listFiles();
            for(int i = 0;i < files.length; i++){
                files[i].delete();
            }
        }
        fileOrPath.delete();
    }

    public static void main(String[] args) throws IOException {
        //获取classpath的绝对路径
        //String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(basePath);

        //处理预先定义好的图片 改变图片大小、加上水印 并压缩
        Thumbnails.of(new File("testImg/car.jpg"))
                .size(400,400)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")),0.5f)
                .outputQuality(0.8f) //压缩
                .toFile("testImg/car-new.jpg");

    }
}
