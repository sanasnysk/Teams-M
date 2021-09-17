package com.sansany.theteams.Contents;

public class Journals {
    //Contacts Journal Columns Name
    String id;
    String j_id;
    String j_date;
    String s_name;
    String j_one;
    String t_leader;
    String j_memo;
    String s_id;
    String t_id;

    public Journals(String id, String j_id, String j_date, String s_name, String j_one, String t_leader, String j_memo, String s_id, String t_id) {
        this.id = id;
        this.j_id = j_id;
        this.j_date = j_date;
        this.s_name = s_name;
        this.j_one = j_one;
        this.t_leader = t_leader;
        this.j_memo = j_memo;
        this.s_id = s_id;
        this.t_id = t_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJ_id() {
        return j_id;
    }

    public void setJ_id(String j_id) {
        this.j_id = j_id;
    }

    public String getJ_date() {
        return j_date;
    }

    public void setJ_date(String j_date) {
        this.j_date = j_date;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getJ_one() {
        return j_one;
    }

    public void setJ_one(String j_one) {
        this.j_one = j_one;
    }

    public String getT_leader() {
        return t_leader;
    }

    public void setT_leader(String t_leader) {
        this.t_leader = t_leader;
    }

    public String getJ_memo() {
        return j_memo;
    }

    public void setJ_memo(String j_memo) {
        this.j_memo = j_memo;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }
}
