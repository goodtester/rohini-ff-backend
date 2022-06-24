/**
 * 
 */
package com.rohini.fastfoodapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * @author Rohini
 */
public class CloudinaryService {

	Cloudinary cloudinary;

	public CloudinaryService() {
		cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dbfozvfop", "api_key", "698592879725188",
				"api_secret", "EIF6axYHeZGfANvCBZ5Gd7e9jOo", "secure", true));
	}

	@SuppressWarnings("rawtypes")
	public String upload(MultipartFile multipartFile, String folder) throws IOException {
		String nameImage = multipartFile.getOriginalFilename().split("\\.")[0];
		File file = convert(multipartFile);
		Map params = ObjectUtils.asMap("public_id", folder + "/" + nameImage, "overwrite", true);
		Map result = cloudinary.uploader().upload(file, params);
		file.delete();
		return result.get("url").toString();
	}

	@SuppressWarnings("rawtypes")
	public Map delete(String id, String folder) throws IOException {
		Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
		return result;
	}

	public File convert(MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(multipartFile.getBytes());
		fo.close();
		return file;
	}

}
