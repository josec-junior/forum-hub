package br.com.alura.forum_hub.domain.topico;

import br.com.alura.forum_hub.domain.curso.CursoRepository;
import br.com.alura.forum_hub.domain.usuario.UsuarioRepository;
import br.com.alura.forum_hub.dtos.TopicoAtualizacaoDTO;
import br.com.alura.forum_hub.dtos.TopicoCadastroDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Topico cadastrarNovoTopico(@Valid TopicoCadastroDTO dados) {
        var topicoExistente = topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem());
        if (topicoExistente) {
            throw new IllegalArgumentException("Já existe um tópico com este título e mensagem!");
        }
        var autor = usuarioRepository.findById(dados.idAutor())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado!"));
        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado!"));
        var topico = new Topico(dados, autor, curso);
        return topicoRepository.save(topico);
    }

    @Transactional
    public void atualizarTopico(Topico topico, TopicoAtualizacaoDTO dados) {
        topico.atualizarInformacoes(dados);
    }
}