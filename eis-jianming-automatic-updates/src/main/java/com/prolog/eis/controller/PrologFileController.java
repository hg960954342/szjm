package com.prolog.eis.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prolog.eis.service.FileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "下载服务")
public class PrologFileController {

	@Autowired
	private FileService FileService;
	@Value("${clientPath}")
	private String clientPath;

	@ApiOperation(value = "查询文件版本", notes = "查询文件版本")
	@ResponseBody
	@RequestMapping(value = "/api/v1/eau/versionUpdate/file/version", method = RequestMethod.POST, produces = "text/plain")
	public String queryFileVersion() throws Exception {
		String str = FileService.fileVersio();
		return str;
	}

	private byte[] readFile(String filePath, int startPosition) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		int dowloadLength = 1 * 1024*1024;
		int fileLength = fis.available();
		if (fileLength - startPosition < dowloadLength) {
			dowloadLength = fileLength - startPosition;
		}
		byte[] buff = new byte[dowloadLength];
		try {
			fis.skip(startPosition);
			fis.read(buff, 0, buff.length);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff;
	}

	private byte[] getErrorBytes(String errorStr) throws UnsupportedEncodingException {
		byte[] errorByte = errorStr.getBytes("Unicode");
		return errorByte;
	}

	@ApiOperation(value = "下载客户端", notes = "下载客户端")
	@ResponseBody
	@RequestMapping(value = "/api/v1/eau/versionUpdate/file/download", method = RequestMethod.POST, produces = "text/plain")
	public ResponseEntity<byte[]> downloadClient(@RequestBody String str) throws Exception {
		String[] strs = str.split("@@");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", new String(strs[0].getBytes("UTF-8"), "ISO8859-1"));
		if (3 == strs.length) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			String filePath = clientPath + strs[0];// 文件路径
			int startPosition = Integer.valueOf(strs[2]);// 文件开始读取位置
			byte[] buff = readFile(filePath, startPosition);
			outStream.write((byte) 1);// 代表读取文件成功
			outStream.write(buff);
			outStream.close();
			return new ResponseEntity<byte[]>(outStream.toByteArray(), headers, HttpStatus.CREATED);
		} else {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			outStream.write((byte) 0);// 代表读取文件失败
			outStream.write(getErrorBytes("数据传输格式不正确！"));
			outStream.close();
			return new ResponseEntity<byte[]>(outStream.toByteArray(), headers, HttpStatus.CREATED);
		}
	}

}
