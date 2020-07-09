package com.homepig;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author homepig
 */
public class Worker {
	public static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		// *注意* 以下代码不要用try catch 包括
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("106.12.89.237");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("fc.987");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//		DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
//			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//			System.out.println(" [x] Received '" + message + "'");
//			try {
//				System.out.println(" [x] 开始处理消息...");
//				doWork(message);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} finally {
//				System.out.println(" [x] Done.");
//			}
//		});
//		channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
//		});
		channel.basicConsume(TASK_QUEUE_NAME, false, "a-consumer-tag", new DefaultConsumer(channel) {
			/**
			 * No-op implementation of {@link Consumer#handleDelivery}.
			 *
			 * @param consumerTag
			 * @param envelope
			 * @param properties
			 * @param body
			 */
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				long deliveryTag = envelope.getDeliveryTag();
				String message = new String(body, StandardCharsets.UTF_8);
				System.out.println(" [x] 开始处理消息...");
				try {
					doWork(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [x] Done.");
					channel.basicAck(deliveryTag, false);
				}
			}
		});
	}

	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.') {
				Thread.sleep(1000);
			}
		}
	}
}
