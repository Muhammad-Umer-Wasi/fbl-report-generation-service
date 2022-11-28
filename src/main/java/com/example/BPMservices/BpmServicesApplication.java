package com.example.BPMservices;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
// import org.apache.commons.io.FileUtils;
@SpringBootApplication
@EnableScheduling
public class BpmServicesApplication implements CommandLineRunner{
	/**
	 *
	 */
	public static void main(String[] args){
		SpringApplication.run(BpmServicesApplication.class, args);
	}
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 */
	public void run(String... args) throws Exception{
		generateReport();
	}

	// public void run()
	@Scheduled(fixedDelay = 10000,initialDelay = 100000)
	public void generateReport() throws Exception{
		try {
			int ID=91;
			List<CustomerRecord> allCustomerRecords = jdbcTemplate.query("SELECT * FROM BULK_ACCOUNT_CUSTOMER_ENTRIES WHERE EXCEL_ID="+ID,
			(rs, rowNum) -> new CustomerRecord(rs.getInt("ID"),rs.getInt("EXCEL_ID"),rs.getTimestamp("CREATED_AT"),rs.getString("CREATED_BY"),rs.getString("CUSTOMER_RECORD"),rs.getString("STATUS")));
			 
			
			TemplateLoader loader = new ClassPathTemplateLoader("/handlebars", ".html");
			  Handlebars handlebars = new Handlebars(loader);
			  Template template = handlebars.compile("template");
			String templateString=template.apply(allCustomerRecords);
		
			System.out.println(templateString);
				
				try {
					FileWriter myWriter = new FileWriter("filename.html");
					myWriter.write(templateString);
					myWriter.close();
					
					}finally {
						
					}
			allCustomerRecords.forEach(us -> {
			String one=us.getCustomerData();
			// System.out.println(" outer: " + one);
			String[] parts=one.split("^");
			  ArrayList<String> elephantList = new ArrayList<>(Arrays.asList(us.getCustomerData().split("\\^")));
			  System.out.println(" Inner 1: " + elephantList);
			  elephantList.forEach(record->{
				System.out.println(record);	
			  });	  
			});
		  
		} catch (Exception ex) {
			System.out.println("error running thread " + ex.getMessage());
		}
	 }
	// @Override
    // public void run(String... args) throws Exception {

	
	// int ID=91;
	// List<CustomerRecord> allCustomerRecords = jdbcTemplate.query("SELECT * FROM BULK_ACCOUNT_CUSTOMER_ENTRIES WHERE EXCEL_ID="+ID,
    // (rs, rowNum) -> new CustomerRecord(rs.getInt("ID"),rs.getInt("EXCEL_ID"),rs.getTimestamp("CREATED_AT"),rs.getString("CREATED_BY"),rs.getString("CUSTOMER_RECORD"),rs.getString("STATUS")));
	 
	
	// TemplateLoader loader = new ClassPathTemplateLoader("/handlebars", ".html");
	//   Handlebars handlebars = new Handlebars(loader);
	//   Template template = handlebars.compile("template");
	// String templateString=template.apply(allCustomerRecords);

	// System.out.println(templateString);
		
	// 	try {
	// 		FileWriter myWriter = new FileWriter("filename.html");
	// 		myWriter.write(templateString);
	// 		myWriter.close();
			
	// 		}finally {
				
	// 		}
	// allCustomerRecords.forEach(us -> {
	// String one=us.getCustomerData();
	// // System.out.println(" outer: " + one);
	// String[] parts=one.split("^");
	//   ArrayList<String> elephantList = new ArrayList<>(Arrays.asList(us.getCustomerData().split("\\^")));
	//   System.out.println(" Inner 1: " + elephantList);
	//   elephantList.forEach(record->{
	// 	System.out.println(record);	
	//   });	  
    // });
	// batchUpdate = jdbcTemplate.batchUpdate("UPDATE BULK_ACCOUNT_EXCEL_SHEET SET STATUS='Complete' WHERE ID="+ID);
	// }
}
