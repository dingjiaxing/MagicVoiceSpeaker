package biz.home.model;

import biz.home.util.ContactInfoDao;

/**
 * Created by admin on 2015/8/19.
 */
public class ContactInfo {
    private String tel;
    private String name;
    private String namePinyin;
    public ContactInfo findByTel(String tel){
        ContactInfo ci=new ContactInfo();

        return ci;
    }
    public ContactInfo() {
    }

    public ContactInfo(String tel) {
        this.tel = tel;
    }

    public ContactInfo(String tel, String name, String namePinyin) {
        this.tel = tel;
        this.name = name;
        this.namePinyin = namePinyin;
    }

    public ContactInfo(String tel, String name) {
        this.tel = tel;
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setName(String name) {
        this.name = name;
    }
}
