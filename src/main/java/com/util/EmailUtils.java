package com.util;

import com.config.EmailConfig;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author super lollipop
 * @date 20-2-13
 */
@Component
public class EmailUtils {

    private static EmailConfig EMAIL_CONFIG;

    @Autowired
    private EmailConfig emailConfig;

    @PostConstruct
    public void init(){
        EMAIL_CONFIG = emailConfig;
    }
    /**发送邮箱验证码
     * @param emailAddress 收件人邮箱地址
     * @param code 验证码
     * @return boolean*/
    public static boolean sendEmail(String emailAddress,String code) throws IOException {

        try {
            SimpleEmail simpleEmail = new SimpleEmail();
            simpleEmail.setHostName(EMAIL_CONFIG.getHostname()); //发件服务器
            simpleEmail.setFrom(EMAIL_CONFIG.getUsername());   //发件账号
            simpleEmail.setAuthentication(EMAIL_CONFIG.getUsername(),EMAIL_CONFIG.getPassword());  //发件邮箱登录验证
            simpleEmail.setCharset("utf-8");    //字符编码
            simpleEmail.addTo(emailAddress);    //发送地址
            simpleEmail.setSubject("音乐播放器注册验证");
            simpleEmail.setMsg("尊敬的用户您好，您本次注册的验证码是:" + code + "." + "一分钟内有效。");
            simpleEmail.send();
            System.out.println("send email...");
            return true;
        }catch (EmailException e){
            return false;
        }

    }

    /**随机生成六位数字*/
    public static String generateCode(){
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }


}
