package com.example.prac_sboot_security.sbootsecurity;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbootsecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(SbootsecurityApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

    }

}
