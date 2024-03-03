package com.example.bluetoothscan;

public class DataValue {
    boolean isSelected;
    String time;
    String dataType;
    String serialNumber;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DataValue() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DataValue(String time, String dataType, String serialNumber) {
        this.time = time;
        this.dataType = dataType;
        this.serialNumber = serialNumber;
    }
}
