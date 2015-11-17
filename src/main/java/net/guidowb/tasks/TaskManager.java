package net.guidowb.tasks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.receptor.client.ReceptorClient;
import org.cloudfoundry.receptor.commands.TaskCreateRequest;
import org.cloudfoundry.receptor.commands.TaskResponse;

public class TaskManager {

	private ReceptorClient client = null;
	private String domain = "task-manager-" + UUID.randomUUID().toString();
	private Map<String, TaskStatus> tasks = new HashMap<String, TaskStatus>();

	public static class TaskStatus {
		String id;
		TaskCreateRequest request = null;
		TaskResponse response = null;
		
		public TaskStatus(TaskCreateRequest request) { this.id = request.getTaskGuid(); this.request = request; }
	}

	private TaskManager(ReceptorClient client) {
		this.client = client;
	}

	public static TaskManager create(String url) {
		ReceptorClient client = new ReceptorClient(url);
		return new TaskManager(client);
	}

	private void registerTask(TaskCreateRequest request) {
		String id = request.getTaskGuid();
		TaskStatus status = new TaskStatus(request);
		tasks.put(id,  status);
	}

	private void updateTask(TaskResponse response) {
		String id = response.getTaskGuid();
		TaskStatus status = tasks.get(id);
		status.response = response;
	}

	public void submitTask(String command, String... args) {
		TaskCreateRequest request = new TaskCreateRequest();
		request.setTaskGuid(UUID.randomUUID().toString());
		request.setDomain(domain);
		request.setRootfs("docker:///cloudfoundry/cflinuxfs2");
		request.runAction.setPath(command);
		request.runAction.setArgs(args);
		registerTask(request);
		client.createTask(request);
	}

	public void submitMonitoredTask(String command, String... args) {
		TaskCreateRequest request = new TaskCreateRequest();
		request.setTaskGuid(UUID.randomUUID().toString());
		request.setDomain(domain);
		request.setRootfs("docker:///cloudfoundry/cflinuxfs2");
		request.runAction.setPath(command);
		request.runAction.setArgs(args);
		request.setCompletionCallbackUrl(getCallbackURL());
		registerTask(request);
		client.createTask(request);
	}

	public Iterable<TaskStatus> getTasks() {
		return tasks.values();
	}

	public TaskResponse getTask(String guid) {
		TaskResponse response = client.getTask(guid);
		updateTask(response);
		return response;
	}

	private String getCallbackURL() {
		String host = System.getenv("CF_INSTANCE_ADDR");
		if (host == null) {
			try {
				host = InetAddress.getLocalHost().getCanonicalHostName() + ":8080";
				System.err.println("Guessing that the callback host is " + host);
			}
			catch (UnknownHostException ex) {
				throw new RuntimeException("Can not determine hostname for callback URL", ex);
			}
		}
		return "http://" + host + "/tasks/complete";
	}

	public void completeTask(TaskResponse task) {
		System.err.println("Completing task " + task.getTaskGuid());
		updateTask(task);
	}
}
