package br.com.alura.forum_hub.dtos;

import br.com.alura.forum_hub.domain.topico.StatusTopico;
import br.com.alura.forum_hub.domain.topico.Topico;

import java.time.LocalDateTime;

public record TopicoListagemDTO(
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        StatusTopico status,
        String autor,
        String curso
) {

    public TopicoListagemDTO(Topico dados) {
        this(
                dados.getTitulo(),
                dados.getMensagem(),
                dados.getDataCriacao(),
                dados.getStatus(),
                dados.getAutor().getNome(),
                dados.getCurso().getNome()
        );
    }
}
