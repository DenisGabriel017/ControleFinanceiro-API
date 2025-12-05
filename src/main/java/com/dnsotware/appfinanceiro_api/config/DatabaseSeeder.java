package com.dnsotware.appfinanceiro_api.config;

import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public DatabaseSeeder(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoriaRepository.count() == 0) {

            Categoria c1 = new Categoria("Alimentação", "DESPESA", null);
            Categoria c2 = new Categoria("Salário", "RECEITA", null);
            Categoria c3 = new Categoria("Lazer", "DESPESA", null);
            Categoria c4 = new Categoria("Transporte", "DESPESA", null);
            Categoria c5 = new Categoria("Educação", "DESPESA", null);

            categoriaRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));

            System.out.println(">>> Categorias padrão criadas com sucesso!");
        }
    }
}