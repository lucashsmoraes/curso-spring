package com.curso.spring;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.curso.spring.domain.*;
import com.curso.spring.enums.EstadoPagamento;
import com.curso.spring.enums.TipoCliente;
import com.curso.spring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CursospringApplication implements CommandLineRunner{
	



	public static void main(String[] args) {
		SpringApplication.run(CursospringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
