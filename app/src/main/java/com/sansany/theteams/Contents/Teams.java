package com.sansany.theteams.Contents;

public class Teams {
    //-- Contacts Team Columns Name
    String id;
    String t_id;
    String t_leader;
    String t_mobile;
    String t_date;
    String t_Memo;

    public Teams(String id, String t_id, String t_leader, String t_mobile, String t_date, String t_Memo) {
        this.id = id;
        this.t_id = t_id;
        this.t_leader = t_leader;
        this.t_mobile = t_mobile;
        this.t_date = t_date;
        this.t_Memo = t_Memo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_leader() {
        return t_leader;
    }

    public void setT_leader(String t_leader) {
        this.t_leader = t_leader;
    }

    public String getT_mobile() {
        return t_mobile;
    }

    public void setT_mobile(String t_mobile) {
        this.t_mobile = t_mobile;
    }

    public String getT_date() {
        return t_date;
    }

    public void setT_date(String t_date) {
        this.t_date = t_date;
    }

    public String getT_Memo() {
        return t_Memo;
    }

    public void setT_Memo(String t_Memo) {
        this.t_Memo = t_Memo;
    }
}
