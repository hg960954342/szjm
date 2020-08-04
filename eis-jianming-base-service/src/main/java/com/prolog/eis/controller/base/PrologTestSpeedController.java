package com.prolog.eis.controller.base;

import com.prolog.eis.util.PrologApiJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@Api(tags="网速测试")
@RequestMapping("/api/v1/base/testspeed")
public class PrologTestSpeedController {

	@ApiOperation(value="下载测试",notes="下载测试")
	@PostMapping("/testdownloadspeed")
	public ResponseEntity<byte[]> testDownloadSpeed(@RequestBody String json) throws Exception{

		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int lengh = helper.getInt("lengh");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		outStream.write(new byte[lengh]);
		return new ResponseEntity<byte[]>(outStream.toByteArray(), headers, HttpStatus.CREATED);
	}
	
	@ApiOperation(value="上传测试",notes="上传测试")
	@PostMapping("/testuploadspeed")
	public void testUploadSpeed() throws Exception{
	}
}
