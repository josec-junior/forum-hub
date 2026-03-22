package br.com.alura.forum_hub.dtos;

import br.com.alura.forum_hub.domain.topico.Topico;
import jakarta.validation.constraints.NotNull;

public record TopicoAtualizacaoDTO(
        @NotNull
        Long id,
        String titulo,
        String mensagem
) {
    public TopicoAtualizacaoDTO(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem()
        );
    }
}