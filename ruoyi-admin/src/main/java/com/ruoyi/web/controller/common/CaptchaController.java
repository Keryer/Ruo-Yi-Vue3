package com.ruoyi.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 验证码操作处理
 * 
 * @author ruoyi
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysConfigService configService;
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        AjaxResult ajax = AjaxResult.success();                                 //建立一个ajax对象，并将其初始化
        boolean captchaEnabled = configService.selectCaptchaEnabled();          //获取是否启用验证码功能
        ajax.put("captchaEnabled", captchaEnabled);                             //以键值对的形式加入ajax对象中
        if (!captchaEnabled)
        {
            return ajax;                                                        //如果不开启，直接将ajax对象返回
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();                                     //生成一个随机的UUID
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;              //设置验证码为随机的UUID

        String capStr = null, code = null;                                      //capStr对应
        BufferedImage image = null;                                             //新建一个图像对象

        // 生成验证码
        String captchaType = RuoYiConfig.getCaptchaType();                      //获取设定的验证码形式
        if ("math".equals(captchaType))                                         //数学计算验证码
        {
            String capText = captchaProducerMath.createText();                  //生成一个数学计算式子
            capStr = capText.substring(0, capText.lastIndexOf("@"));        //
            code = capText.substring(capText.lastIndexOf("@") + 1);//
            image = captchaProducerMath.createImage(capStr);                    //
        }
        else if ("char".equals(captchaType))                                    //输入字符验证码
        {
            capStr = code = captchaProducer.createText();                       //生成一个字符串，并把答案设置为字符串
            image = captchaProducer.createImage(capStr);                        //把字符串转化为图片编码
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);                         //将生成的验证码图片输出到开始建立的空图片中
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());                            //输出异常
        }

        ajax.put("uuid", uuid);                                                 //将随机的UUID加入ajax对象
        ajax.put("img", Base64.encode(os.toByteArray()));                       //将最终的验证码图片加入ajax图像
        return ajax;
    }
}
