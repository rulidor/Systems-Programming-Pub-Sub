package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;

public class HolderAnswerAndMore {
    // class result to pass information from subscriber to anther

    private List<String> agentsNames;
    private int moneyPennySerialNum;
    private  int Qtime;
    private boolean ans;


    public HolderAnswerAndMore()
    {
        agentsNames =new ArrayList<>();
        moneyPennySerialNum =0;
        ans =false;
        Qtime=0;
    }

    public int getQtime() {
        return Qtime;
    }

    public void setQtime(int qtime) {
        Qtime = qtime;
    }

    public boolean isAns() {
        return ans;
    }

    public void setAgentsNames(List<String> agentsNames) {
        this.agentsNames = agentsNames;
    }

    public void setMoneyPennySerialNum(int moneyPennySerialNum) {
        this.moneyPennySerialNum = moneyPennySerialNum;
    }


    public List<String> getAgentsNames() {
        return agentsNames;
    }

    public boolean answer() {
        return ans;
    }

    public void setAns(boolean ans) {
        this.ans = ans;
    }

    public int getMoneyPennySerialNum() {
        return moneyPennySerialNum;
    }





}