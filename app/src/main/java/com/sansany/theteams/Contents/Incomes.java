package com.sansany.theteams.Contents;

public class Incomes {
    //Contacts Incomes Columns Name
    String id;
    String i_id;
    String i_date;
    String t_leader;
    String i_collect;
    String i_tax;
    String i_memo;
    String t_id;

    public Incomes(String id, String i_id, String i_date, String t_leader, String i_collect, String i_tax, String i_memo, String t_id) {
        this.id = id;
        this.i_id = i_id;
        this.i_date = i_date;
        this.t_leader = t_leader;
        this.i_collect = i_collect;
        this.i_tax = i_tax;
        this.i_memo = i_memo;
        this.t_id = t_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
    }

    public String getI_date() {
        return i_date;
    }

    public void setI_date(String i_date) {
        this.i_date = i_date;
    }

    public String getT_leader() {
        return t_leader;
    }

    public void setT_leader(String t_leader) {
        this.t_leader = t_leader;
    }

    public String getI_collect() {
        return i_collect;
    }

    public void setI_collect(String i_collect) {
        this.i_collect = i_collect;
    }

    public String getI_tax() {
        return i_tax;
    }

    public void setI_tax(String i_tax) {
        this.i_tax = i_tax;
    }

    public String getI_memo() {
        return i_memo;
    }

    public void setI_memo(String i_memo) {
        this.i_memo = i_memo;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }
}
