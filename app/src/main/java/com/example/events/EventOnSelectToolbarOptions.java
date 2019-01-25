package com.example.events;

public class EventOnSelectToolbarOptions {


    /**
     * 1 = Reset
     */

    private int whichItemSelected;

    public int getWhichItemSelected() {
        return whichItemSelected;
    }

    public void setWhichItemSelected(int whichItemSelected) {
        this.whichItemSelected = whichItemSelected;
    }

    public EventOnSelectToolbarOptions(int whichItemSelected) {
        this.whichItemSelected = whichItemSelected;
    }
}
