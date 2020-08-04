package com.prolog.eis.service.impl;

import com.prolog.eis.dto.base.FileVersionRespDto;
import com.prolog.eis.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileServiceImpl implements FileService {
	
	@Value("${clientPath}")
	private String clientPath;

	@Override
	public String fileVersio() throws Exception {
		String path = clientPath;
		File file = new File(path);
		File[] listfile = file.listFiles();
		if (0 == listfile.length) {
			return "False##没有找到更新包文件!";
		}
		if (listfile.length > 1) {
			return "False##发现多个更新包文件!";
		}
		FileVersionRespDto fileVersionRespDto = new FileVersionRespDto();
		String fileName = listfile[0].getName();
		try {
			String[] str = fileName.split("\\.");
			if (str.length == 5) {
				fileVersionRespDto.setPart1(Integer.valueOf(str[0]));
				fileVersionRespDto.setPart2(Integer.valueOf(str[1]));
				fileVersionRespDto.setPart3(Integer.valueOf(str[2]));
				fileVersionRespDto.setPart4(Integer.valueOf(str[3]));
			}
			fileName = "True##" + fileName;
			fileVersionRespDto.setFileName(fileName);
			fileVersionRespDto.setFileLength(listfile[0].length());
			return fileVersionRespDto.toString();

		} catch (Exception e) {
			return "False##" + e.getMessage();
		}

	}
}
