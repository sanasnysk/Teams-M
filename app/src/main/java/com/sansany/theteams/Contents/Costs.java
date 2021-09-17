package com.sansany.theteams.Contents;

public class Costs {
    //Contents Costs Columns Name
    String id;
    String c_id;
    String c_date;
    String s_name;
    String c_contents;
    String c_amount;
    String c_memo;
    String s_id;

    public Costs(String id, String c_id, String c_date, String s_name, String c_contents, String c_amount, String c_memo, String s_id) {
        this.id = id;
        this.c_id = c_id;
        this.c_date = c_date;
        this.s_name = s_name;
        this.c_contents = c_contents;
        this.c_amount = c_amount;
        this.c_memo = c_memo;
        this.s_id = s_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getC_contents() {
        return c_contents;
    }

    public void setC_contents(String c_contents) {
        this.c_contents = c_contents;
    }

    public String getC_amount() {
        return c_amount;
    }

    public void setC_amount(String c_amount) {
        this.c_amount = c_amount;
    }

    public String getC_memo() {
        return c_memo;
    }

    public void setC_memo(String c_memo) {
        this.c_memo = c_memo;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }
}
