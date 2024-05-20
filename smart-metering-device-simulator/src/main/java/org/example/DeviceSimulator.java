package org.example;

import com.rabbitmq.client.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;

public class DeviceSimulator {

    private static final String QUEUE_NAME = "devices_queue";
    private static final String RABBITMQ_HOST = "localhost";
    private static final int RABBITMQ_PORT = 5672;
    private static final String RABBITMQ_USERNAME = "rabbit";
    private static final String RABBITMQ_PASSWORD = "root";

    private static final String CONFIG_FILE_PATH = "/home/ioana/Desktop/Energy Management System/smart-metering-device-simulator/src/main/resources/device_config.properties";
    private static final String SENSOR_DATA_FILE_PATH = "/home/ioana/Desktop/Energy Management System/smart-metering-device-simulator/src/main/resources/sensor.csv";
    private static BufferedReader sensorDataReader;


    public static void main(String[] args) {
        String deviceId;

        do {
            deviceId = readDeviceIdFromConfig();

            if (!isValidUUID(deviceId)) {
                System.out.println("Invalid UUID format. Please enter a valid UUID.");
            }
        } while (!isValidUUID(deviceId));

        initializeSensorReader();
        startSimulatedDataSending(deviceId);
    }
    private static boolean isValidUUID(String deviceId) {
        try {
            UUID.fromString(deviceId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    private static String readDeviceIdFromKeyboard() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the deviceId:");
            return reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readDeviceIdFromConfig() {
        Properties properties = new Properties();

        try (InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(inputStream);
            return properties.getProperty("deviceId");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void startSimulatedDataSending(String deviceId) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RABBITMQ_HOST);
            factory.setPort(RABBITMQ_PORT);
            factory.setUsername(RABBITMQ_USERNAME);
            factory.setPassword(RABBITMQ_PASSWORD);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);


            executorService.scheduleAtFixedRate(() -> {
                double measurementValue = readNextMeasurementValueFromSensor();
                if (measurementValue != Double.MIN_VALUE) {
                    try {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String timestamp = now.format(formatter);
                        DeviceData deviceData = new DeviceData(timestamp, deviceId, measurementValue);
                        Gson gson = new Gson();
                        String jsonMessage = gson.toJson(deviceData);
                        channel.basicPublish("", QUEUE_NAME, null, jsonMessage.getBytes());
                        System.out.println(" [x] Sent '" + jsonMessage + "'");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 10, TimeUnit.SECONDS);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void initializeSensorReader() {
        try {
            sensorDataReader = new BufferedReader(new FileReader(SENSOR_DATA_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double readNextMeasurementValueFromSensor() {
        try {
            if (sensorDataReader != null) {
                String line = sensorDataReader.readLine();
                if (line != null) {
                    return Double.parseDouble(line);
                } else {
                    sensorDataReader.close();
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return Double.MIN_VALUE;
    }


}
