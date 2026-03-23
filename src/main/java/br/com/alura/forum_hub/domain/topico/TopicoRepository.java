package br.com.alura.forum_hub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensagem(@NotBlank(message = "O título para o tópico é obrigatório!") String titulo, @NotBlank(message = "Você deve fornecer uma descrição para o tópico!") String mensagem);
}