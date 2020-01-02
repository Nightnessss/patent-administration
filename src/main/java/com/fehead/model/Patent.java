package com.fehead.model;

import java.sql.Date;
import java.sql.Timestamp;

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
 * @author Nightnessss 2019/12/20 9:57
 */
public class Patent {
    private int id;
    private String code;
    private String name;
    private String inventor;
    private String applicant;
    private Date publicationTime;

    public Patent(int id, String code, String name, String inventor, String applicant, Date publicationTime) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.inventor = inventor;
        this.applicant = applicant;
        this.publicationTime = publicationTime;
    }

    public Patent(String code, String name, String inventor, String applicant, Date publicationTime) {       this.code = code;
        this.name = name;
        this.inventor = inventor;
        this.applicant = applicant;
        this.publicationTime = publicationTime;
    }

    public Patent() {
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInventor() {
        return inventor;
    }

    public void setInventor(String inventor) {
        this.inventor = inventor;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public Date getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(Date publicationTime) {
        this.publicationTime = publicationTime;
    }

    @Override
    public String toString() {
        return "Patent{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", inventor='" + inventor + '\'' +
                ", applicant='" + applicant + '\'' +
                ", publicationTime=" + publicationTime +
                '}';
    }
}
