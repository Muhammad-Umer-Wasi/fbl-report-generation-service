package com.example.BPMservices;

public class LookUp {
    private String Lookup_Hidden_Value;
    private String Lookup_Visible_Value;
    public LookUp(String lookup_Visible_Value) {
        Lookup_Visible_Value = lookup_Visible_Value;
    }
    // public LookUp(String lookup_Hidden_Value) {
    //     Lookup_Hidden_Value = lookup_Hidden_Value;
    // }
    public LookUp(String lookup_Hidden_Value, String lookup_Visible_Value) {
        Lookup_Hidden_Value = lookup_Hidden_Value;
        Lookup_Visible_Value = lookup_Visible_Value;
    }
    public LookUp() {
    }
    public String getLookup_Hidden_Value() {
        return Lookup_Hidden_Value;
    }
    public void setLookup_Hidden_Value(String lookup_Hidden_Value) {
        Lookup_Hidden_Value = lookup_Hidden_Value;
    }
    public String getLookup_Visible_Value() {
        return Lookup_Visible_Value;
    }
    public void setLookup_Visible_Value(String lookup_Visible_Value) {
        Lookup_Visible_Value = lookup_Visible_Value;
    }
    
}
