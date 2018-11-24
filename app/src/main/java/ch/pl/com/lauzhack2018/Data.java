package ch.pl.com.lauzhack2018;

import org.json.JSONException;
import org.json.JSONObject;

public class Data {
    private String timestamp;
    private int ID;
    private String jobName;
    private int machineState;
    private int machineSpeed;
    private int machineSpeedMax;
    private int inputCounter;
    private int outputCounter;
    private int cuttingForce;
    private int cuttingForceMax;
    private boolean normalStop;
    private boolean urgentStop;
    private boolean openProtection;
    private boolean technicalDefect;

    public Data(JSONObject jsonObject) throws JSONException {

        this.timestamp = jsonObject.getString("timestamp");
        this.ID = jsonObject.getInt("ID");
        this.jobName = jsonObject.getString("JobName");
        this.machineState = jsonObject.getInt("MachineState");
        this.machineSpeed = jsonObject.getInt("MachineSpeed");
        this.machineSpeedMax = jsonObject.getInt("MachineSpeedMax");
        this.inputCounter = jsonObject.getInt("InputCounter");
        this.outputCounter = jsonObject.getInt("OutputCounter");
        this.cuttingForce = jsonObject.getInt("CuttingForce");
        this.cuttingForceMax = jsonObject.getInt("CuttingForceMax");
        this.normalStop = jsonObject.getInt("NormalStop") == 1;
        this.urgentStop = jsonObject.getInt("UrgentStop") == 1;
        this.openProtection = jsonObject.getInt("OpenProtection") == 1;
        this.technicalDefect = jsonObject.getInt("TechnicalDefect") == 1;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getID() {
        return ID;
    }

    public String getJobName() {
        return jobName;
    }

    public int getMachineState() {
        return machineState;
    }

    public int getMachineSpeed() {
        return machineSpeed;
    }

    public int getMachineSpeedMax() {
        return machineSpeedMax;
    }

    public int getInputCounter() {
        return inputCounter;
    }

    public int getOutputCounter() {
        return outputCounter;
    }

    public int getCuttingForce() {
        return cuttingForce;
    }

    public int getCuttingForceMax() {
        return cuttingForceMax;
    }

    public boolean getNormalStop() {
        return normalStop;
    }

    public boolean getUrgentStop() { return urgentStop; }

    public boolean getOpenProtection() {
        return openProtection;
    }

    public boolean getTechnicalDefect() {
        return technicalDefect;
    }
}
