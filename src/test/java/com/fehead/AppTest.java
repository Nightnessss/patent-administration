package com.fehead;

import static org.junit.Assert.assertTrue;

import com.fehead.dao.PatentDao;
import com.fehead.utils.AESUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
//        PatentDao patentDao  = new PatentDao();
//        System.out.println(patentDao.getPassword("admin"));
        System.out.println(AESUtil.encrypt("123456", "admin"));
        System.out.println(AESUtil.decrypt("MJaaB4RNAmzWqFuYkFmf9Q==", "admin"));
    }
}
