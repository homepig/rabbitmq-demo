package com.homepig;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

/**
 * @author homepig
 */
public class Send {
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("106.12.89.237");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("fc.987");
		try (Connection connection = connectionFactory.newConnection();
			 Channel channel = connection.createChannel();) {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "hello world!";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			System.out.println(" [x] Send '" + message + "'");
		}
	}
}
