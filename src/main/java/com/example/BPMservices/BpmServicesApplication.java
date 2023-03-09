package com.example.BPMservices;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
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

@SpringBootApplication
@EnableScheduling
public class BpmServicesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BpmServicesApplication.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void run(String... args) throws Exception {
		generateReport();
	}

	@Scheduled(fixedDelay = 120000, initialDelay = 120000)
	public void generateReport() {
		System.out.println("Generate Report CRON job working!");

		// ArrayList ar=new ArrayList();

		try {

			List<LookUp> lookupRecords = jdbcTemplate.query(
					"SELECT LOOKUP_VISIBLE_VALUE, LOOKUP_HIDDEN_VALUE from LOOKUP where PARENT_LOOKUP_TYPE='Bank Account'",
					(rs, rowNum) -> {

						return new LookUp(rs.getString("LOOKUP_HIDDEN_VALUE"), rs.getString("LOOKUP_VISIBLE_VALUE"));
					});
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();

			List<LookUp> list = new ArrayList<LookUp>();
			Map<String, String> row = new HashMap<String, String>();
			lookupRecords.forEach(us -> {
				row.put(us.getLookup_Hidden_Value(), us.getLookup_Visible_Value());
				LookUp LookUp = new LookUp(null, null);
				LookUp.setLookup_Hidden_Value(us.getLookup_Hidden_Value());
				LookUp.setLookup_Visible_Value(us.getLookup_Visible_Value());
				list.add(LookUp);

			});
			data.add(row);

			Map<Integer, String> excelSheets = new HashMap<>();

			List<CustomerRecord> allCustomerRecords = jdbcTemplate.query(
					"SELECT customerEntries.*, excelSheet.EXCEL_NAME FROM BULK_ACCOUNT_CUSTOMER_ENTRIES customerEntries INNER JOIN BULK_ACCOUNT_EXCEL_SHEET excelSheet ON customerEntries.EXCEL_ID = excelSheet.ID AND customerEntries.status = 'Valid' AND excelSheet.IS_REPORT_GEN_REQUESTED = 1 AND excelSheet.STATUS = 'Inprogress' AND excelSheet.ARE_INSTANCES_CREATED = 0",
					(rs, rowNum) -> {
						Integer excelId = rs.getInt("EXCEL_ID");
						String excelName = rs.getString("EXCEL_NAME");
						String customerRecord = rs.getString("CUSTOMER_RECORD");
						excelSheets.putIfAbsent(excelId, excelName);
						ArrayList<String> customerRecordDetails = new ArrayList<>(
								Arrays.asList(customerRecord.split("\\^")));
						String productCode = customerRecordDetails.get(28);

						CustomerRecord accountTyRecord = new CustomerRecord();
						row.forEach((k, v) -> {

							if (k.equals(productCode)) {

								accountTyRecord.setAccountType(v);

							}

						});

						return new CustomerRecord(rs.getInt("ID"), excelId,
								rs.getTimestamp("CREATED_AT"),
								rs.getString("CREATED_BY"),
								rs.getString("CUSTOMER_RECORD"),
								rs.getString("STATUS"),
								rs.getString("ERROR_MESSAGE"), accountTyRecord.getAccountType());

					});

			TemplateLoader loader = new ClassPathTemplateLoader("/handlebars", ".html");
			Handlebars handlebars = new Handlebars(loader);
			Template template = handlebars.compile("template");

			Map<Integer, List<CustomerRecord>> groupedCustomerRecords = allCustomerRecords.stream()
					.collect(Collectors.groupingBy(record -> record.getExcelID()));

			groupedCustomerRecords.forEach((key, records) -> {
				try {
					String fileName = excelSheets.get(key);
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					File reportFile = new File("reports/" + fileName + "-" + key + "/report.html");
					reportFile.getParentFile().mkdirs();
					String templateString = template.apply(records);
					FileWriter fileWriter = new FileWriter(reportFile);
					fileWriter.write(templateString);
					fileWriter.close();
				} catch (IOException e) {
					System.out.println("Error while writing to reports");
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			});

			List<Integer> unqiueExcelSheetIDs = new ArrayList<>(excelSheets.keySet());

			System.out.println("EXCEL SHEET RECORDS TO BE UPDATED: " + unqiueExcelSheetIDs);

			List<Object[]> batch = new ArrayList<>();

			unqiueExcelSheetIDs.forEach(entry -> {
				Object[] values = new Object[] {
						"Completed",
						entry,
				};
				batch.add(values);
			});

			jdbcTemplate.batchUpdate("UPDATE BULK_ACCOUNT_EXCEL_SHEET SET STATUS = ? WHERE ID = ?", batch);

			System.out.println("Report Generated!");
			System.out.println("Successfully updated the database.");

		} catch (Exception ex) {
			System.out.println("error running thread " + ex.getMessage());
		}
	}
}
