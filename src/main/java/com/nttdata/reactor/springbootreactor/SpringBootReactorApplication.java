package com.nttdata.reactor.springbootreactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nttdata.reactor.springbootreactor.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> nombres = Flux.just("Jostin Martinez","Mario Doom", "Luis Torres","Guillermo Bacca","Oscar Lopez", "Bruce Lee", "Bruce Willis");
			
		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(),nombre.split(" ")[1].toUpperCase()))
			.filter(n -> n.getNombre().equalsIgnoreCase("bruce"))
			.doOnNext(e -> {
				if(e == null){
					throw new RuntimeException("Nombres no pueden ser vacios!");
				}else{
					System.out.println(e);
				}
			})
			.map(usuario -> {
				String nombre = usuario.getNombre().toLowerCase().concat(" ").concat(usuario.getApellido().toLowerCase());
				usuario.setNombre(nombre);
				return usuario;
			});

			usuarios.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {

					@Override
					public void run() {
						log.info("Finalizado la ejecucion del Observable");
						
					}
					
				});

	}

}
