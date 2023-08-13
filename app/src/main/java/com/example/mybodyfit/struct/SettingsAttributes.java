package com.example.mybodyfit.struct;

public class SettingsAttributes extends Attributes{

    public SettingsAttributes(String type, Integer drawable) {
        this.type = type;
        this.drawable = drawable;
    }

    @Override
    public String getType() {
        return type;
    }

    public Integer getDrawable() {
        return drawable;
    }
}
