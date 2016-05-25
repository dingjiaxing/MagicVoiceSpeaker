package biz.home.model;

import org.litepal.crud.DataSupport;

/**
 * Created by lmw on 2015/7/28.
 *
 * This list is used for recording the information of user.
 * Information includes:
 * 1.Personal Information.
 * 2.System's states of functions.
 */
public class UserInfo extends DataSupport{

    //Personal Information
    private String tel;
    private String name;
    private String company;
    private String gender;

    //System's states of functions.
    // "0" means close and "1" means open.
    private int stateItem1;
    private int stateItem2;
    private int stateItem3;
    private int stateItem4;
    private int stateItem5;
    private int stateItem6;

    public UserInfo(){
        stateItem1=0;
        stateItem2=0;
        stateItem3=0;
        stateItem4=0;
        stateItem5=0;
        stateItem6=0;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStateItem1() {
        return stateItem1;
    }

    public void setStateItem1(int stateItem1) {
        this.stateItem1 = stateItem1;
    }

    public int getStateItem2() {
        return stateItem2;
    }

    public void setStateItem2(int stateItem2) {
        this.stateItem2 = stateItem2;
    }

    public int getStateItem3() {
        return stateItem3;
    }

    public void setStateItem3(int stateItem3) {
        this.stateItem3 = stateItem3;
    }

    public int getStateItem4() {
        return stateItem4;
    }

    public void setStateItem4(int stateItem4) {
        this.stateItem4 = stateItem4;
    }

    public int getStateItem5() {
        return stateItem5;
    }

    public void setStateItem5(int stateItem5) {
        this.stateItem5 = stateItem5;
    }

    public int getStateItem6() {
        return stateItem6;
    }

    public void setStateItem6(int stateItem6) {
        this.stateItem6 = stateItem6;
    }
}
