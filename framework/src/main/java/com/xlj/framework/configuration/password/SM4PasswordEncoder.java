package com.xlj.framework.configuration.password;

import com.xlj.common.sgcc.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Security;
import java.util.Objects;

/**
 * SM4加密
 *
 * @author zhangkun
 */
@Slf4j
public class SM4PasswordEncoder implements PasswordEncoder {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private Sm4Utils sm4Utils;

    public SM4PasswordEncoder(Sm4Utils sm4Utils) {
        this.sm4Utils = sm4Utils;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword != null) {
            try {
                return sm4Utils.encrypt(rawPassword.toString());
            } catch (Exception e) {
                log.error("加密失败");
                return rawPassword.toString();
            }
        }
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        try {
            return Objects.equals(rawPassword, encodedPassword)
                    /*sm2解密再sm4加密之后和密码对比*/
                    || Objects.equals(encode(/*Sm2Utils.decrypt(*/rawPassword.toString()/*)*/), encodedPassword)
                    /*sm2解密之后同密码对比，用于oauth2的password模式的basic认证*/
                    || Objects.equals(/*Sm2Utils.decrypt(*/rawPassword.toString()/*)*/, encodedPassword);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("密码对比错误");
            return false;
        }
    }

}
