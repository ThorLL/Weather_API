package com.weatherapi.app.weatherapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot generated class for starting the program.
 * Added reader for the commandline arguments to read the inputted api key.
 */
@SpringBootApplication
public class WeatherapiApplication {
	private static String apiKey;

	public static String getApiKey() {
		return apiKey;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			if (args.length == 0)
				System.out.println("No api key for Open Weather Map given, please provide one");
			else
				System.out.println("Two many argument please only provide your Open Weather Map api key");

			System.exit(0);
		}
		apiKey = args[0];
		SpringApplication.run(WeatherapiApplication.class, args);
	}
}
