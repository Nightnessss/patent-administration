package com.fehead;

import static org.junit.Assert.assertTrue;

import com.fehead.dao.PatentDao;
import com.fehead.error.BusinessException;
import com.fehead.model.Patent;
import com.fehead.utils.AESUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        PatentDao patentDao  = new PatentDao();
        System.out.println(patentDao.getPassword("admin"));
//        System.out.println(AESUtil.encrypt("123456", "admin"));
//        System.out.println(AESUtil.decrypt("MJaaB4RNAmzWqFuYkFmf9Q==", "admin"));
    }

    @Test
    public void whenGetPasswordSuccess() {
        PatentDao patentDao  = new PatentDao();
        System.out.println(new ReflectionToStringBuilder(patentDao.getPassword("admin")));
    }

    @Test
    public void whenSelectSuccess() throws BusinessException {
        PatentDao patentDao  = new PatentDao();
        List<Patent> res1 = patentDao.getPatents();
        List<Patent> res2 = patentDao.getPatentsByCode("CN209622437U");
        List<Patent> res3 = patentDao.getPatentsByName("自");
        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }

    @Test
    public void whenAddSuccess() throws BusinessException, SQLException {
        PatentDao patentDao  = new PatentDao();
        Patent patent = new Patent(16, "CN209806514U", "园林绿化用多功能修剪装置", "何君洁;贲宗友;梁梦婷;贾荣;王科鑫",
                "滁州学院", new Date(1576771200));
        patentDao.addPatent(patent);
        System.out.println(patentDao.getPatentsByCode("CN209806514U"));
    }

    @Test
    public void whenUpdateSuccess() throws BusinessException {
        PatentDao patentDao  = new PatentDao();
        Patent patent = new Patent(16, "CN209806514U", "园林绿化用多功能修剪装置", "何君洁;贲宗友;梁梦婷;贾荣;王科鑫",
                "滁州学院change", new Date(1576771200));
        patentDao.updatePatent(patent);
        System.out.println(patentDao.getPatentsByCode("CN209806514U"));
    }

    @Test
    public void whenDeleteSuccess() throws BusinessException, SQLException {
        PatentDao patentDao  = new PatentDao();
        List<Integer> ids = new ArrayList<>();
        ids.add(14);
        ids.add(16);
        patentDao.deletePatents(ids);
        System.out.println(patentDao.getPatents());
    }
}
