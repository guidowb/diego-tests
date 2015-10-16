package net.guidowb.tasks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.cloudfoundry.receptor.client.ReceptorClient;
import org.cloudfoundry.receptor.commands.TaskCreateRequest;

public class TaskManager {

	private ReceptorClient client = null;
	private String domain = "task-manager-" + UUID.randomUUID().toString();

	private TaskManager(ReceptorClient client) {
		this.client = client;
	}

	public static TaskManager create(String url) {
		ReceptorClient client = new ReceptorClient(url);
		return new TaskManager(client);
	}

	public void submitTask(String command, String... args) {
		TaskCreateRequest request = new TaskCreateRequest();
		request.setTaskGuid(UUID.randomUUID().toString());
		request.setDomain(domain);
		request.setRootfs("docker:///cloudfoundry/cflinuxfs2");
		request.runAction.setPath(command);
		request.runAction.setArgs(args);
		request.setCompletionCallbackUrl(getCallbackURL());
		client.createTask(request);
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
}
