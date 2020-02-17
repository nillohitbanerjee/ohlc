package com.upstock.dto;

public class Tread {

    private String sym;
    private String t;
    private String q;
    private String p;
    private String ts;
    private String side;
    private String ts2;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getTs2() {
        return ts2;
    }

    public void setTs2(String ts2) {
        this.ts2 = ts2;
    }

    @Override
    public String toString() {
        return "Tread{" +
                "sym='" + sym + '\'' +
                ", t='" + t + '\'' +
                ", q='" + p+ '\'' +
                ", p='" + q + '\'' +
                ", ts='" + ts + '\'' +
                ", side='" + side + '\'' +
                ", ts2='" + ts2 + '\'' +
                '}';
    }
}
