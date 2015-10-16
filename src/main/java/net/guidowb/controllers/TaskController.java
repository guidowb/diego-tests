package net.guidowb.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@RequestMapping(value="/complete", method=RequestMethod.POST)
	public void completeTask(HttpServletRequest request) throws IOException {
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Enumeration<String> values = request.getHeaders(name);
			while (values.hasMoreElements()) {
				System.err.println("   " + name + ": " + values.nextElement());
			}
		}
		BufferedReader reader = request.getReader();
		while (reader.ready()) {
			String line = reader.readLine();
			System.err.println(line);
		}
	}
}
