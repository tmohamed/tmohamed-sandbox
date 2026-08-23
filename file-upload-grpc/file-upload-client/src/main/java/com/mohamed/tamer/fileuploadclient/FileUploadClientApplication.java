package com.mohamed.tamer.fileuploadclient;

import com.mohamed.tamer.file_upload_with_timeout.FileUploadWithTimeoutResponse;
import com.mohamed.tamer.fileupload.grpc.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.io.File;

@SpringBootApplication
public class FileUploadClientApplication {

	@Autowired private FileUploadClientService fileUploadClientService;
	@Autowired private HelloClientService helloClientService;
	@Autowired private FileUploadWithTimeoutClientService fileUploadWithTimeoutClientService;

	public static void main(String[] args) {
		SpringApplication.run(FileUploadClientApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {

//			File file = new File("/Users/tamer-abdeltawab/Desktop/TEMP/8-2026/13/test.png");
//			FileUploadResponse response = fileUploadClientService.uploadFile(file).join();
//			System.out.println("Upload complete! Server status: " + response.getStatus());

//			helloClientService.sayHello("Tamer Mohamed");
//			System.out.println("Hello request sent! Waiting for response...");

			File file = new File("/Users/tamer-abdeltawab/Desktop/TEMP/8-2026/13/test.png");
			FileUploadWithTimeoutResponse response = fileUploadWithTimeoutClientService.uploadFileWithTimeout(file).join();
			System.out.println("Upload complete! Server status: " + response.getStatus());

		};
	}
}
