package br.com.alura.forum_hub.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoCadastroDTO(
        @NotBlank(message = "O título para o tópico é obrigatório!")
        String titulo,
        @NotBlank(message = "Você deve fornecer uma descrição para o tópico!")
        String mensagem,
        @NotNull(message = "O autor do tópico é obrigatório!")
        Long idAutor,
        @NotNull(message = "O curso correspondente ao tópico é obrigatório!")
        Long idCurso
) {
}