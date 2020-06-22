/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import java.io.Serializable;

public class Standard {

    private boolean bfe;
    private boolean pfe;
    private boolean vfe;
    private String special_standard;

    public Standard() {
        bfe = false;
        pfe = false;
        vfe = false;
        special_standard = "No ASTM/KF standard";
    }

    public Standard(boolean bfe, boolean pfe, boolean vfe, String special_standard) {
        this.bfe = bfe;
        this.pfe = pfe;
        this.vfe = vfe;
        this.special_standard = special_standard;
    }

    public boolean isBfe() {
        return bfe;
    }

    public void setBfe(boolean bfe) {
        this.bfe = bfe;
    }

    public boolean isPfe() {
        return pfe;
    }

    public void setPfe(boolean pfe) {
        this.pfe = pfe;
    }

    public boolean isVfe() {
        return vfe;
    }

    public void setVfe(boolean vfe) {
        this.vfe = vfe;
    }

    public String getSpecial_standard() {
        return special_standard;
    }

    public void setSpecial_standard(String special_standard) {
        this.special_standard = special_standard;
    }
}
