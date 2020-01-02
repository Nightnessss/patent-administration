package com.fehead.controller;

import com.alibaba.fastjson.JSONObject;
import com.fehead.dao.PatentDao;
import com.fehead.error.BusinessException;
import com.fehead.error.EmBusinessError;
import com.fehead.model.Patent;
import com.fehead.response.CommonReturnType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
 * @author Nightnessss 2019/12/20 14:10
 */
@RestController
@RequestMapping("/patent")
@CrossOrigin("*")
public class PatentController extends BaseController {
    private PatentDao patentDao = new PatentDao();
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 专利标识代码
    static final String[] IDENTIFICATION = {"A", "A8", "U", "U8", "U9", "Y1", "Y2", "S",
                                            "S9", "S1", "S2"};

    @GetMapping("/getPatents")
    public CommonReturnType getPatents() throws BusinessException {

        List<Patent> patents = new ArrayList<>();
        try {
            patents = patentDao.getPatents();
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.DATABASE_OPERATION_ERROR);
        }
        logger.info("RETURN: " + patents);
        return CommonReturnType.create(patents);
    }

    @PostMapping("/deletePatents")
    public CommonReturnType deletePatents(@RequestParam("ids") String ids) throws BusinessException {
        List<Integer> idList = JSONObject.parseArray(ids, Integer.class);
//        System.out.println(idList);
        try {
            patentDao.deletePatents(idList);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.DATABASE_OPERATION_ERROR);
        }
        return CommonReturnType.create(null);
    }

    @PostMapping("/addPatent")
    public CommonReturnType addPatents(@RequestParam("id") int id,
                                       @RequestParam("code") String code,
                                       @RequestParam("name") String name,
                                       @RequestParam("inventor") String inventor,
                                       @RequestParam("applicant") String applicant,
                                       @RequestParam("publication_time") Date publication_time) throws BusinessException {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(name) || StringUtils.isEmpty(inventor)
                || StringUtils.isEmpty(applicant) || publication_time == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        codeValidation(code);

        Patent patent = new Patent(code, name, inventor, applicant, publication_time);

        try {
            patentDao.addPatent(patent);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.DATABASE_OPERATION_ERROR);
        }
        return CommonReturnType.create(null);
    }

    @PostMapping("/updatePatent")
    public CommonReturnType updatePatents(@RequestParam("id") int id,
                                          @RequestParam("code") String code,
                                          @RequestParam("name") String name,
                                          @RequestParam("inventor") String inventor,
                                          @RequestParam("applicant") String applicant,
                                          @RequestParam("publication_time") Date publication_time) throws BusinessException {
        Patent patent = new Patent(id, code, name, inventor, applicant, publication_time);

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(name) || StringUtils.isEmpty(inventor)
                || StringUtils.isEmpty(applicant) || publication_time == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        codeValidation(code);

        try {
            patentDao.updatePatent(patent);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.DATABASE_OPERATION_ERROR);
        }
        return CommonReturnType.create(null);
    }

    @GetMapping("/getPatentsByCode")
    public CommonReturnType getPatentsByCode(@RequestParam("code") String code) throws BusinessException {
        List<Patent> patents = new ArrayList<>();

        patents = patentDao.getPatentsByCode(code);
        return CommonReturnType.create(patents);

    }

    @GetMapping("/getPatentsByName")
    public CommonReturnType getPatentsByName(@RequestParam("name") String name) throws BusinessException {
        List<Patent> patents = new ArrayList<>();

        patents = patentDao.getPatentsByName(name);

        logger.info("RETURN: " + new ReflectionToStringBuilder(patents));
        return CommonReturnType.create(patents);

    }


    /**
     * 公开号验证
     * @param code
     * @return
     * @throws BusinessException
     */
    private void codeValidation(String code) throws BusinessException {
        code = StringUtils.trim(code);

        // 国家代码
        String country = StringUtils.substring(code, 0, 2);
//        System.out.println("country:" + country);
        // 判断国家代码否为大写字母
        if (!country.matches("^[A-Z]+$")) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "公开号国家代码错误");
        }

        // 专利类型
        String type = StringUtils.substring(code, 2, 3);
//        System.out.println("type:" + type);
        // 2位实用新型专利
        if (!type.equals("2")) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "公开号专利类型错误");
        }

        // 文献流水号
        String serial = StringUtils.substring(code, 3, 11);
//        System.out.println("serial:" + serial);
        // 判断文件流水号是否为数字
        if (!serial.matches("^[0-9]+$")) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "公开号文献流水号错误");
        }

        // 标识代码
        String identification = StringUtils.substring(code, 11);
//        System.out.println("identification:" + identification);
        for (String s : IDENTIFICATION) {
            if (identification.equals(s)) return;
        }

        throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "公开号表示代码错误");
    }
}
