package pivotal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PerftestApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("http://localhost:8080/actuator/health");
	}

}
