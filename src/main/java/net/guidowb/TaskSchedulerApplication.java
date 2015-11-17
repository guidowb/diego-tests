package net.guidowb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.guidowb.tasks.TaskManager;

@SpringBootApplication
@Configuration
public class TaskSchedulerApplication {

	@Bean
	public TaskManager getTaskManager() {
		return TaskManager.create("receptor.local.lattice.cf");
	}

    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulerApplication.class, args);
    }
}
