package biz.home.model;

/**
 * Created by admin on 2015/9/18.
 */
public class UserSetting {
    //System's states of functions.
    // "0" means close and "1" means open.
    private String telephone;
    private int stateItem1;
    private int stateItem2;
    private int stateItem3;
    private int stateItem4;
    private int stateItem5;
    private int stateItem6;
    public UserSetting(){
        stateItem1=0;
        stateItem2=0;
        stateItem3=0;
        stateItem4=0;
        stateItem5=0;
        stateItem6=0;
    }
    public UserSetting(String telephone,int stateItem1,int stateItem2,int stateItem3,int stateItem4,int stateItem5,int stateItem6){
        this.telephone=telephone;
        this.stateItem1=stateItem1;
        this.stateItem2=stateItem2;
        this.stateItem3=stateItem3;
        this.stateItem4=stateItem4;
        this.stateItem5=stateItem5;
        this.stateItem6=stateItem6;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    @Override
    public String toString() {
        return "UserSetting{" +
                "telephone='" + telephone + '\'' +
                ", stateItem1=" + stateItem1 +
                ", stateItem2=" + stateItem2 +
                ", stateItem3=" + stateItem3 +
                ", stateItem4=" + stateItem4 +
                ", stateItem5=" + stateItem5 +
                ", stateItem6=" + stateItem6 +
                '}';
    }
}
