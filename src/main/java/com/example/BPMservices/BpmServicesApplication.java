package com.example.BPMservices;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	/**
	 *
	 */
	public static void main(String[] args) {
		SpringApplication.run(BpmServicesApplication.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 */
	public void run(String... args) throws Exception {
		generateReport();
	}

	@Scheduled(fixedDelay = 120000, initialDelay = 120000)
	public void generateReport() throws Exception {
		System.out.println("Generate Report CRON job working!");

		try {
			ArrayList<Integer> excelSheetIDs = new ArrayList();
			List<CustomerRecord> allCustomerRecords = jdbcTemplate.query(
					"SELECT customerEntries.* FROM BULK_ACCOUNT_CUSTOMER_ENTRIES customerEntries INNER JOIN BULK_ACCOUNT_EXCEL_SHEET excelSheet ON customerEntries.EXCEL_ID = excelSheet.id AND customerEntries.status = 'Valid' AND excelSheet.IS_REPORT_GEN_REQUESTED = 1 AND excelSheet.STATUS = 'Inprogress' AND excelSheet.ARE_INSTANCES_CREATED = 0",
					(rs, rowNum) -> {
						excelSheetIDs.add(rs.getInt("EXCEL_ID"));
						return new CustomerRecord(rs.getInt("ID"), rs.getInt("EXCEL_ID"),
								rs.getTimestamp("CREATED_AT"),
								rs.getString("CREATED_BY"), rs.getString("CUSTOMER_RECORD"),
								rs.getString("STATUS"));
					});

			TemplateLoader loader = new ClassPathTemplateLoader("/handlebars", ".html");
			Handlebars handlebars = new Handlebars(loader);
			Template template = handlebars.compile("template");
			String templateString = template.apply(allCustomerRecords);

			FileWriter myWriter = new FileWriter("filename.html");
			myWriter.write(templateString);
			myWriter.close();

			List<Integer> unqiueExcelSheetIDs = excelSheetIDs.stream().distinct().collect(Collectors.toList());

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
