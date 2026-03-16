package br.com.alura.forum_hub.domain.topico;

import br.com.alura.forum_hub.domain.curso.CursoRepository;
import br.com.alura.forum_hub.domain.usuario.UsuarioRepository;
import br.com.alura.forum_hub.dtos.TopicoCadastroDTO;
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
        var autor = usuarioRepository.findById(dados.idAutor())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));
        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado!"));

        var topico = new Topico(dados, autor, curso);
        return topicoRepository.save(topico);
    }
}