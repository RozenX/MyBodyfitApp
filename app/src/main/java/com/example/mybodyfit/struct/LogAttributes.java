package com.example.mybodyfit.struct;

public class LogAttributes extends Attributes {

    public LogAttributes (String type, Integer drawable) {
        this.type = type;
        this.drawable = drawable;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Integer getDrawable() {
        return drawable;
    }
}
