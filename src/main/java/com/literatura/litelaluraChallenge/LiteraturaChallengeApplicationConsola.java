package com.literatura.litelaluraChallenge;
import com.literatura.litelaluraChallenge.Principal.Principal;
import com.literatura.litelaluraChallenge.repository.IAutorRepository;
import com.literatura.litelaluraChallenge.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaChallengeApplicationConsola implements CommandLineRunner {

	@Autowired
	private IBookRepository libroRepository;
	@Autowired
	private IAutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaChallengeApplicationConsola.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(libroRepository, autorRepository);
		principal.mostrarMenu();

	}
}
