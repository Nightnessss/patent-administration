package com.fehead.dao;

import com.fehead.error.BusinessException;
import com.fehead.error.EmBusinessError;
import com.fehead.model.Password;
import com.fehead.model.Patent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
 * @author Nightnessss 2019/12/20 9:50
 */
public class PatentDao {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/javadesign" +
            "?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";

    static final String USER = "root";
    static final String PASS = "1948730080";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Connection con;

    public PatentDao() {
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询密码
     *
     * @param username
     * @return
     */
    public Password getPassword(String username) {
        Password pass = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_password WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            pass = new Password(rs.getInt(1), rs.getString(2), rs.getString(3));
            ps.close();
            //关闭SQL语句执行对象
            //con.close();
            //关闭数据库连接对象
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pass;
    }

    /**
     * 根据公开号查询专利
     *
     * @param code
     * @return
     */
    public List<Patent> getPatentsByCode(String code) throws BusinessException {
        List<Patent> patents = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM patent WHERE code=?");
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            patents = select(rs);
            ps.close();
            //关闭SQL语句执行对象
            //con.close();
            //关闭数据库连接对象
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patents;
    }


    /**
     * 根据名称来查询专利
     *
     * @param sname
     * @return
     */
    public List<Patent> getPatentsByName(String sname) throws BusinessException {
        List<Patent> result = new ArrayList<Patent>();
        try {
            String strSQL = "SELECT * FROM patent WHERE name LIKE '%" + sname + "%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strSQL);
            result = select(rs);
            st.close();
            //关闭SQL语句执行对象
            //con.close();
            //关闭数据库连接对象
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    public List<Patent> getPatents() {
        List<Patent> result = new ArrayList<Patent>();
        try {
            String strSQL = "SELECT * FROM patent";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strSQL);
            result = select(rs);
            st.close();
            //关闭SQL语句执行对象
            //con.close();
            //关闭数据库连接对象
        } catch (SQLException | BusinessException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 查找通用
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<Patent> select(ResultSet rs) throws SQLException, BusinessException {

        List<Patent> result = new ArrayList<Patent>();

        while (rs.next()) {
            int id = rs.getInt(1);
            String code = rs.getString(2);
            String name = rs.getString(3);
            String inventor = rs.getString(4);
            String applicant = rs.getString(5);
            Date publicationTime = rs.getDate(6);
            Patent patent = new Patent(id, code, name, inventor, applicant, publicationTime);
            result.add(patent);
        }
        if (result.size() == 0) {
            throw new BusinessException(EmBusinessError.RETURN_PARAMETER_ERROR, "未查到相关专利");
        }
        return result;
    }


    /**
     * 批量删除
     *
     * @param ids
     */
    public void deletePatents(List<Integer> ids) throws SQLException {
        String strSQL = "DELETE FROM patent WHERE id IN (";
        strSQL = strSQL + '\'' + ids.get(0) + '\'';
        for (int i = 1; i < ids.size(); i++) {
            strSQL = strSQL + "," + '\'' + ids.get(i) + '\'';
        }
        strSQL += ')';
//        System.out.println(strSQL);
        Statement st = con.createStatement();
        st.execute(strSQL);
        logger.info("DELETE: " + ids);
        st.close();
        //关闭SQL语句执行对象
        //con.close();
        //关闭数据库连接对象
    }


    /**
     * 添加数据
     *
     * @param patent
     */
    public void addPatent(Patent patent) throws SQLException {

//        try{
        PreparedStatement ps = con.prepareStatement("INSERT INTO " +
                "patent(code, name, inventor, applicant, publication_time) VALUES(?,?,?,?,?)");
        //创建SQL语句执行对象
        ps.setString(1, patent.getCode());
        ps.setString(2, patent.getName());
        ps.setString(3, patent.getInventor());
        ps.setString(4, patent.getApplicant());
        ps.setDate(5, patent.getPublicationTime());
        ps.execute();
        ps.close();
        //关闭SQL语句执行对象
        //con.close();
        //关闭数据库连接对象
//        }
//        catch (SQLException e) {//捕捉处理数据连接或者操作异常
//            e.printStackTrace();
//        }
    }


    /**
     * 更新
     *
     * @param patent
     */
    public void updatePatent(Patent patent) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE patent " +
                    "SET code=?,name=?,inventor=?,applicant=?, publication_time=? WHERE id=?");
            //创建SQL语句执行对象
            ps.setString(1, patent.getCode());
            ps.setString(2, patent.getName());
            ps.setString(3, patent.getInventor());
            ps.setString(4, patent.getApplicant());
            ps.setDate(5, patent.getPublicationTime());
            ps.setInt(6, patent.getId());
            ps.execute();
            logger.info("UPDATE: " + patent.getCode());

            ps.close();
            //关闭SQL语句执行对象
            //con.close();
            //关闭数据库连接对象
        } catch (SQLException e) {//捕捉处理数据连接或者操作异常
            e.printStackTrace();
        }
    }

}
