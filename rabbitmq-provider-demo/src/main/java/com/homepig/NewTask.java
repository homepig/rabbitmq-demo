package com.homepig;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author homepig
 */
public class NewTask {
	public static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		args = new String[]{"1", "2", "3", "....................."};
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("106.12.89.237");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("fc.987");
		try (Connection connection = connectionFactory.newConnection();
			 Channel channel = connection.createChannel();) {
			channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
			String message = String.join(" ", args);
			channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			System.out.println(" [x] Sent '" + message + "'");
		}
	}
}
