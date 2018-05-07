package models;

/**
 * Created by shrinivas on 4/6/18.
 */

public class CallDetail {

    private int id;
    private String name;
    private String phoneNumber;
    private String bridgeNumber;
    private String passcode;

    public CallDetail(int id, String name, String phoneNumber, String bridgeNumber, String passcode) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bridgeNumber = bridgeNumber;
        this.passcode = passcode;

    }

    public CallDetail() {

    }

    public CallDetail(String callName) {
        name = callName;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBridgeNumber() {
        return bridgeNumber;
    }

    public void setBridgeNumber(String bridgeNumber) {
        this.bridgeNumber = bridgeNumber;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }


}
