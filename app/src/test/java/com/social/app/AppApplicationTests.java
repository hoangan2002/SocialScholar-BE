package com.social.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;

@SpringBootTest
class AppApplicationTests {

	@Test
	String contextLoads() {

		return String.valueOf(Paths.get("uploads").toAbsolutePath()+ File.pathSeparator);
	}


}
