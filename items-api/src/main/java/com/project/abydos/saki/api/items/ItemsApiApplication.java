package com.project.abydos.saki.api.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.project.abydos.saki.common",
		"com.project.abydos.saki.api.items",
		"com.project.schale.saki.database.product"
})
public class ItemsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemsApiApplication.class, args);
	}

}
