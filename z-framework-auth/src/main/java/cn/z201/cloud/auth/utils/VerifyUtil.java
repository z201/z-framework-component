package cn.z201.cloud.auth.utils;

import cn.z201.cloud.auth.utils.dto.VerifyImageDto;
import com.wf.captcha.ArithmeticCaptcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author z201.coding@gmail.com
 **/
public class VerifyUtil {

    // 过期时间是90秒，既是一分半
    public static final long EXPIRATION = 90L;

    // 验证码字符集
    private static final char[] chars = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    // 字符数量
    private static final int SIZE = 4;
    // 干扰线数量
    private static final int LINES = 4;
    // 宽度
    private static final int WIDTH = 90;
    // 高度
    private static final int HEIGHT = 50;
    // 字体大小
    private static final int FONT_SIZE = 28;

    /**
     * 生成随机验证码及图片
     */
    public static VerifyImageDto createImage() {
        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        // 5.画随机字符
        Random ran = new Random();
        for (int i = 0; i < SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(
                    chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
                    ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        // 7.返回验证码和图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return VerifyImageDto.builder()
                .code(sb.toString())
                .bos(baos)
                .build();
    }

    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));
        return color;
    }

    public static VerifyImageDto arithmeticCaptcha() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(WIDTH, HEIGHT);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        captcha.out(baos);
        return VerifyImageDto.builder()
                .code(captcha.text())
                .bos(baos)
                .build();
    }

}
