package net.guidowb.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.cloudfoundry.receptor.commands.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.guidowb.tasks.TaskManager;
import net.guidowb.tasks.TaskManager.TaskStatus;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired TaskManager taskManager;

	@RequestMapping(method=RequestMethod.GET)
	public Iterable<TaskStatus> listTasks() {
		return taskManager.getTasks();
	}

	@RequestMapping(method=RequestMethod.POST)
	public void submitTask() {
		taskManager.submitMonitoredTask("sleep", "15");
	}

	@RequestMapping(value="/complete", method=RequestMethod.POST)
	public void completeTask(@RequestBody TaskResponse task) throws IOException {
		taskManager.completeTask(task);
	}

	@RequestMapping(value="/complete-debug", method=RequestMethod.POST)
	public void completeTaskDebug(HttpServletRequest request) throws IOException {
	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    System.err.println(body);
	}
}
