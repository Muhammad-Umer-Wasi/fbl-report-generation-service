package com.example.BPMservices;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomerRecord {

    private int Id;
    private int ExcelID;
    private Timestamp CreatedAt;
    private String CreatedBy;
    private String CustomerData;
    private String[] CustomerDetails;
    private String Status;
    private String ErrorMessage;
    private String ReportCreatedAt;

    public CustomerRecord(int id, int excelID, Timestamp createdAt, String createdBy, String customerData,
            String status) {
        Id = id;
        ExcelID = excelID;
        CreatedAt = createdAt;
        CreatedBy = createdBy;
        CustomerData = customerData;
        Status = status;
        CustomerDetails = customerData.split("\\^");
        ReportCreatedAt = LocalDateTime.now().toString();
    }

    public CustomerRecord(int excelID) {
        ExcelID = excelID;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getExcelID() {
        return ExcelID;
    }

    public void setExcelID(int excelID) {
        ExcelID = excelID;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        CreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCustomerData() {
        return CustomerData;
    }

    public void setCustomerData(String customerData) {
        CustomerData = customerData;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String[] getCustomerDetails() {
        return CustomerDetails;
    }

    public void setCustomerDetails(String[] customerDetails) {
        CustomerDetails = customerDetails;
    }

    public String getReportCreatedAt() {
        return ReportCreatedAt;
    }

    public void setReportCreatedAt(String reportCreatedAt) {
        ReportCreatedAt = reportCreatedAt;
    }

}
