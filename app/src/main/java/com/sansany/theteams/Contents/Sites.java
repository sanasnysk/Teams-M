package com.sansany.theteams.Contents;

public class Sites {
    //-- Contacts Site Table Columns Name
    String id;
    String s_id;
    String s_name;
    String t_leader;
    String s_pay;
    String s_manager;
    String s_date;
    String s_memo;
    String t_id;

    public Sites(String id, String s_id, String s_name, String t_leader, String s_pay, String s_manager, String s_date, String s_memo, String t_id) {
        this.id = id;
        this.s_id = s_id;
        this.s_name = s_name;
        this.t_leader = t_leader;
        this.s_pay = s_pay;
        this.s_manager = s_manager;
        this.s_date = s_date;
        this.s_memo = s_memo;
        this.t_id = t_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getT_leader() {
        return t_leader;
    }

    public void setT_leader(String t_leader) {
        this.t_leader = t_leader;
    }

    public String getS_pay() {
        return s_pay;
    }

    public void setS_pay(String s_pay) {
        this.s_pay = s_pay;
    }

    public String getS_manager() {
        return s_manager;
    }

    public void setS_manager(String s_manager) {
        this.s_manager = s_manager;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getS_memo() {
        return s_memo;
    }

    public void setS_memo(String s_memo) {
        this.s_memo = s_memo;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }
}
