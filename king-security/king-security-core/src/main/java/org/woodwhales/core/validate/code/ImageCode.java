package org.woodwhales.core.validate.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ImageCode extends ValidateCode {

	private BufferedImage image;

    /**
     *
     * @param image 图片
     * @param code 验证码
     * @param expireIn 多长时间过期,单位秒
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this(image, code, LocalDateTime.now().plusSeconds(expireIn));
    }

    /**
     *
     * @param image 图片
     * @param code 验证码
     * @param expireTime 过期时间
     */
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }

}
