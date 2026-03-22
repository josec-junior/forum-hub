package br.com.alura.forum_hub.controllers;

import br.com.alura.forum_hub.domain.topico.TopicoRepository;
import br.com.alura.forum_hub.domain.topico.TopicoService;
import br.com.alura.forum_hub.dtos.TopicoAtualizacaoDTO;
import br.com.alura.forum_hub.dtos.TopicoCadastroDTO;
import br.com.alura.forum_hub.dtos.TopicoListagemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;
    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarTopico(@RequestBody @Valid TopicoCadastroDTO dados, UriComponentsBuilder uriBuilder) {

        var topico = topicoService.cadastrarNovoTopico(dados);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(topico);
    }

    @GetMapping
    public ResponseEntity<Page<TopicoListagemDTO>> listarTopicos(@PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = topicoRepository.findAll(paginacao)
                .map(TopicoListagemDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoListagemDTO> detalharTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TopicoListagemDTO(topico.get()));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarTopico(@PathVariable Long id, @RequestBody TopicoAtualizacaoDTO dados) {
        var topico = topicoRepository.findById(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        topicoService.atualizarTopico(topico.get(), dados);
        return ResponseEntity.ok(new TopicoListagemDTO(topico.get()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}