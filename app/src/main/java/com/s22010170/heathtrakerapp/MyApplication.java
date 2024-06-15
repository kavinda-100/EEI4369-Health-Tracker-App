package com.s22010170.heathtrakerapp;

import android.app.Application;

public class MyApplication extends Application {
    // TODO:Global Variables
    private String globalVariableEmail = "";
    private String globalVariableName = "";
    private byte[] globalVariableImageAvatar = null;

    // TODO:get the global variable
    public String getGlobalVariableEmail() {
        return globalVariableEmail;
    }
    public String getGlobalVariableName() {
        return globalVariableName;
    }
    public byte[] getGlobalVariableImageAvatar() {
        return globalVariableImageAvatar;
    }
    // TODO:set the global variable
    public void setGlobalVariableEmail(String email) {
        globalVariableEmail = email;
    }
    public void setGlobalVariableName(String name) {
        globalVariableName = name;
    }
    public void setGlobalVariableImageAvatar(byte[] image) {
        globalVariableImageAvatar = image;
    }
    // TODO:clear the global variable
    public void clearGlobalVariableEmail() {
        globalVariableEmail = "";
    }
    public void clearGlobalVariableName() {
        globalVariableName = "";
    }
    public void clearGlobalVariableImage() {
        globalVariableImageAvatar = null;
    }
}
