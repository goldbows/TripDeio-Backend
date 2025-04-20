package com.tripdeio.backend;

import com.tripdeio.backend.service.GraphHopperService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@SpringBootTest(classes = MytourguideApplication.class)  // Explicitly specify the main application class
class MytourguideApplicationTests {
	@Autowired
	private GraphHopperService graphHopperService;  // Example of a service dependency

	@Test
	void contextLoads() {
		assertNotNull(graphHopperService);  // Check if the bean is correctly injected
	}
}
