package com.util;

import com.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author super lollipop
 * @date 5/4/20
 */
@Component
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static ServerConfig SERVER_CONFIG;

    @Autowired
    public void constructor(ServerConfig serverConfig){
        SERVER_CONFIG = serverConfig;
    }

    public static String upload(MultipartFile multipartFile, HttpServletRequest request){
        String imagePath = getImagePath(request);
        System.out.println(imagePath);
        String filename = multipartFile.getOriginalFilename();
        String extendName = filename.substring(filename.lastIndexOf("."));  //扩展名
        File destination = new File(imagePath + File.separator + TimeUtils.getNowTimeFormatString() + extendName);
        try {
            multipartFile.transferTo(destination);
            LOGGER.info("上传成功");
            String prefix = CutUtils.cut(destination.toString(),3);
            String prefixFormat = prefix.replaceAll("\\\\","/");
            String formatPath = destination.getPath().replaceAll("\\\\","/");
            String URL = formatPath.replaceAll(prefixFormat,SERVER_CONFIG.getHostname());
            return URL;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    public static String delete(String URL,String subFolder,HttpServletRequest request){
        if (!StringUtils.isEmpty(URL)){
            File file = new File( getStaticPath(request) + File.separator + subFolder + URL.substring(URL.lastIndexOf("/")));
            if (file.exists()){
                file.delete();
                LOGGER.info("删除文件成功");
                return "success";
            }else {
                LOGGER.info("没有找到资源文件");
            }
        }else {
            LOGGER.info("资源文件为空");
        }
        return "fail";
    }

    private static String getStaticPath(HttpServletRequest request){
        String path = request.getServletContext().getRealPath("/") + File.separator + "static";
        return getPath(path);
    }

    private static String getImagePath(HttpServletRequest request){
        String path = request.getServletContext().getRealPath("/") + File.separator + "static" + File.separator + "image";
        return getPath(path);
    }

    /**根据路径创建路径*/
    private static String getPath(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
