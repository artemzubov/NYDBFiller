package ru.spb.zubov.NyDBFiller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import ru.spb.zubov.NyDBFiller.domain.NYCalendar;
import ru.spb.zubov.NyDBFiller.repo.NYCalendarRepo;
import ru.spb.zubov.NyDBFiller.util.MySAXParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.springframework.util.FileCopyUtils.copy;

@SpringBootApplication
public class NyDbFillerApplication implements CommandLineRunner {

	private final NYCalendarRepo nyCalendarRepo;

	public NyDbFillerApplication(NYCalendarRepo nyCalendarRepo) {
		this.nyCalendarRepo = nyCalendarRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(NyDbFillerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		AtomicInteger counter = new AtomicInteger(1);
		InputStream resource = new ClassPathResource("files").getInputStream();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
			reader.lines().forEach(path -> {
				try {
					File tempXml = File.createTempFile(UUID.randomUUID().toString(), ".xml");
					GZIPInputStream archive= new GZIPInputStream(new FileInputStream(new File(path)));
					extractFromArchiveToTempXml(tempXml, archive);
					parseFileAndPersistEntry(tempXml);
					tempXml.delete();
					System.out.println("File " + counter.getAndIncrement() + " out of 3260 was processed.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void extractFromArchiveToTempXml(File tempXml, GZIPInputStream archive) throws IOException {
		File tempZip = File.createTempFile(UUID.randomUUID().toString(), ".zip");
		OutputStream out = new FileOutputStream(tempZip);
		copy(archive, out);
		out.close();
		archive.close();

		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZip));
		ZipEntry zipEntry = zis.getNextEntry();
		if (zipEntry != null) {
			FileOutputStream fos = new FileOutputStream(tempXml);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
		}
		zis.closeEntry();
		zis.close();
		tempZip.delete();
	}

	private void parseFileAndPersistEntry(File file) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MySAXParser saxParser = new MySAXParser();

			parser.parse(file, saxParser);

			List<NYCalendar> newData = saxParser.getNewData();
			nyCalendarRepo.saveAll(newData);

			List<NYCalendar> updateCaseData = saxParser.getUpdateCaseData();
			for (NYCalendar nyCalendarWithCase : updateCaseData) {
				for (NYCalendar nyFromDB : nyCalendarRepo.findAllByAppearanceIdAndCaseId(nyCalendarWithCase.getAppearanceId(), null)) {
					nyFromDB.setCaseId(nyCalendarWithCase.getCaseId());
					nyCalendarRepo.save(nyFromDB);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
