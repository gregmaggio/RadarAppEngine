/**
 * 
 */
package ca.datamagic.radar.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Bucket.BlobTargetOption;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.common.collect.Lists;

import ca.datamagic.radar.dto.ImageDTO;
import ca.datamagic.radar.inject.Retry;

/**
 * @author Greg
 *
 */
public class RadarImageDAO extends BaseDAO {
	private static final Logger logger = Logger.getLogger(RadarImageDAO.class.getName());
	private static final Pattern dateTimePattern = Pattern.compile("\\w{4}\\x5F\\w{2}\\x5F\\w{4}\\x5F(?<year>\\d{4})(?<month>\\d{2})(?<day>\\d{2})\\x5F(?<hour>\\d{2})(?<minute>\\d{2})(?<second>\\d{2})\\x2Epng", Pattern.CASE_INSENSITIVE);
	private static final String bucketName = "ca-datamagic-radar";
	private static final String imageCounts = "imageCounts.csv";
	private GoogleCredentials credentials = null;
	private Storage storage = null;
	
	public GoogleCredentials getCredentials() throws FileNotFoundException, IOException {
		if (this.credentials == null) {
			this.credentials = GoogleCredentials.fromStream(new FileInputStream(getKeyFile()))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		}
		return this.credentials;
	}
	
	public Storage getStorage() throws FileNotFoundException, IOException {
		if (this.storage == null) {
			GoogleCredentials credentials = getCredentials();
			this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		}
		return this.storage;
	}
	
	public void saveImageCounts(String imageCountsCsv) throws FileNotFoundException, IOException {
		Storage storage = this.getStorage();
		BlobId blobId = BlobId.of(bucketName, imageCounts);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, imageCountsCsv.getBytes());
	}
	
	public void save(String radar, List<String> sourceImages) throws FileNotFoundException, IOException {
		for (int ii = 0; ii < sourceImages.size(); ii++) {
			this.save(radar, sourceImages.get(ii));
		}
	}
	
	@Retry
	public void save(String radar, String sourceImage) throws FileNotFoundException, IOException {
		String[] sourceImageParts = sourceImage.split("/");
		String fileName = sourceImageParts[sourceImageParts.length - 1];
		String pngFileName = fileName.replace(".tif.gz", ".png");
		Storage storage = this.getStorage();
		BlobId blobId = BlobId.of(bucketName, MessageFormat.format("{0}/{1}", radar, pngFileName));
		Blob blob = storage.get(blobId);
		if ((blob != null) && blob.exists()) {
			return;
		}
		SourceImageDAO sourceImageDAO = new SourceImageDAO();
		ImageDTO image = sourceImageDAO.downloadUnzipConvert(sourceImage);
		ImageConverterDAO converterDAO = new ImageConverterDAO();
		byte[] imageBytes = converterDAO.convertToPNG(image);		
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, imageBytes);
	}
	
	public int clean(String radar) throws FileNotFoundException, IOException {
		List<String> files = this.list(radar);
		Storage storage = this.getStorage();
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long todayMillis = today.getTimeInMillis();
		int deleted = 0;
		for (int ii = 0; ii < files.size(); ii++) {
			String file = files.get(ii);
			logger.info("file: " + file);
			Matcher dateTimeMatcher = dateTimePattern.matcher(file);
			if (dateTimeMatcher.matches()) {
				int year = Integer.parseInt(dateTimeMatcher.group("year"));
				int month = Integer.parseInt(dateTimeMatcher.group("month"));
				int day = Integer.parseInt(dateTimeMatcher.group("day"));
				int hour = Integer.parseInt(dateTimeMatcher.group("hour"));
				int minute = Integer.parseInt(dateTimeMatcher.group("minute"));
				int second = Integer.parseInt(dateTimeMatcher.group("second"));
				Calendar fileTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				fileTime.set(Calendar.YEAR, year);
				fileTime.set(Calendar.MONTH, month - 1);
				fileTime.set(Calendar.DAY_OF_MONTH, day);
				fileTime.set(Calendar.HOUR_OF_DAY, hour);
				fileTime.set(Calendar.MINUTE, minute);
				fileTime.set(Calendar.SECOND, second);
				long fileTimeMillis = fileTime.getTimeInMillis();
				long differenceMillis = todayMillis - fileTimeMillis;
				Duration difference = Duration.ofMillis(differenceMillis);
				if (difference.toHours() > 15) {
					logger.info("Deleting " + file + "...");
					BlobId blobId = BlobId.of(bucketName, MessageFormat.format("{0}/{1}", radar, file));
					if (storage.delete(blobId)) {
						logger.info("...deleted!");
						deleted++;
					} else {
						logger.warning("Error removing " + file);
						break;
					}
				}
			}
		}
		return deleted;
	}
	
	public List<String> list(String radar) throws FileNotFoundException, IOException {
		List<String> files = new ArrayList<String>();
		Storage storage = this.getStorage();
		Bucket bucket = storage.get(bucketName);		
		Page<Blob> blobs = bucket.list(BlobListOption.prefix(radar));
		for (Blob blob : blobs.iterateAll()) {
			String[] blobParts = blob.getName().split("/");
			files.add(blobParts[1]);
		}
		Collections.sort(files);
		return files;
	}
	
	public byte[] load(String radar, String file) throws FileNotFoundException, IOException {
		Storage storage = this.getStorage();
		BlobId blobId = BlobId.of(bucketName, MessageFormat.format("{0}/{1}", radar, file));
		return storage.readAllBytes(blobId);
	}
}
