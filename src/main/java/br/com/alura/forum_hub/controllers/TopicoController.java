package br.com.alura.forum_hub.controllers;

import br.com.alura.forum_hub.domain.topico.TopicoRepository;
import br.com.alura.forum_hub.domain.topico.TopicoService;
import br.com.alura.forum_hub.dtos.TopicoAtualizacaoDTO;
import br.com.alura.forum_hub.dtos.TopicoCadastroDTO;
import br.com.alura.forum_hub.dtos.TopicoListagemDTO;
import br.com.alura.forum_hub.infra.security.SecurityConfigurations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tópicos", description = "Endpoints para o gerenciamento de tópicos.")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class TopicoController {

    @Autowired
    private TopicoService topicoService;
    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastra um novo tópico no fórum", description = "Método para cadastrar um novo tópico no fórum, com validação para evitar tópicos duplicados.")
    @ApiResponse(responseCode = "201", description = "Tópico cadastrado com sucesso!")
    @ApiResponse(responseCode = "400", description = "Já existe um tópico com esse título e mensagem!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    public ResponseEntity<TopicoListagemDTO> cadastrarTopico(@RequestBody @Valid TopicoCadastroDTO dados, UriComponentsBuilder uriBuilder) {

        var topico = topicoService.cadastrarNovoTopico(dados);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoListagemDTO(topico));
    }

    @GetMapping
    @Operation(summary = "Lista todos os tópicos do fórum", description = "Método para fazer a listagem dos tópicos do fórum, utilizando paginação.")
    @ApiResponse(responseCode = "200", description = "Lista de tópicos retornada com sucesso!")
    public ResponseEntity<Page<TopicoListagemDTO>> listarTopicos(@PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = topicoRepository.findAll(paginacao)
                .map(TopicoListagemDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Exibir detalhes de um tópico específico do fórum", description = "Método para exibir as informações de um tópico específico do fórum, sendo buscado pelo ID.")
    @ApiResponse(responseCode = "200", description = "Tópico encontrado!")
    @ApiResponse(responseCode = "404", description = "Tópico não encontrado!")
    public ResponseEntity<TopicoListagemDTO> detalharTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TopicoListagemDTO(topico.get()));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualiza dados de um tópico específico do fórum", description = "Atualiza/edita os dados de um tópico existente, pelo ID.")
    @ApiResponse(responseCode = "200", description = "Tópico atualizado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Tópico não encontrado!")
    public ResponseEntity<TopicoListagemDTO> atualizarTopico(@PathVariable Long id, @RequestBody TopicoAtualizacaoDTO dados) {
        var topico = topicoRepository.findById(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        topicoService.atualizarTopico(topico.get(), dados);
        return ResponseEntity.ok(new TopicoListagemDTO(topico.get()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um tópico existente", description = "Método para deletar um tópico existente, pelo ID.")
    @ApiResponse(responseCode = "204", description = "Tópico deletado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Tópico não encontrado!")
    @Transactional
    public ResponseEntity<Void> deletarTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}