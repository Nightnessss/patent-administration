package com.fehead.controller;

import com.fehead.dao.PatentDao;
import com.fehead.error.BusinessException;
import com.fehead.error.EmBusinessError;
import com.fehead.model.Password;
import com.fehead.response.CommonReturnType;
import com.fehead.utils.AESUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/12/28 19:37
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private PatentDao patentDao = new PatentDao();

    @PostMapping("/login")
    public CommonReturnType login(@RequestParam(value = "username", required = true) String username,
                                  @RequestParam(value = "password", required = true) String password) throws NoSuchAlgorithmException, BusinessException {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "账号或密码不能为空");
        }
        Password pass = null;
        try {
            pass = patentDao.getPassword(username);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.DATABASE_OPERATION_ERROR);
        }
        String p = AESUtil.decrypt(pass.getPassword(), username);
        if (!StringUtils.equals(p, password)) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR, "账号或密码错误");
        }

        String token = Jwts.builder()
//                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(SignatureAlgorithm.HS512, "LoginUser")
                .compact();
        logger.info("LOGIN: " + username);
        return CommonReturnType.create("bearer " + token);
    }

    @RequestMapping("/fail")
    public CommonReturnType fail() throws BusinessException {
        throw new BusinessException(EmBusinessError.LOGIN_ERROR, "请登录");
    }
}
