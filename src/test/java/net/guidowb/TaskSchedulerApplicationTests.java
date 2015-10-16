package net.guidowb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import net.guidowb.tasks.TaskManager;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TaskSchedulerApplication.class)
@WebAppConfiguration
public class TaskSchedulerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void taskCreationSucceeds() throws InterruptedException {
		TaskManager manager = TaskManager.create("receptor.192.168.11.11.xip.io");
		manager.submitTask("echo", "hello", "world!");
	}
}
