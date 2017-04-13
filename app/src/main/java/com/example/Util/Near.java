package com.example.Util;

/**
 * Created by Administrator on 2017/1/15.
 */

public class Near {

    public Near(String name, String date, String imag){
        this.name = name;
        this.date = date;
        this.imag = imag;
    }
    private String imag;
    private String name;
    private String date;
    public String getImag() {
        return imag;
    }
    public void setImag(String imag) {
        this.imag = imag;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
