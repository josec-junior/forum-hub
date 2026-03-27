package br.com.alura.forum_hub.controllers;

import br.com.alura.forum_hub.domain.usuario.Usuario;
import br.com.alura.forum_hub.domain.usuario.UsuarioRepository;
import br.com.alura.forum_hub.dtos.AutenticacaoDTO;
import br.com.alura.forum_hub.dtos.CadastroDTO;
import br.com.alura.forum_hub.dtos.TokenJWTDTO;
import br.com.alura.forum_hub.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoits para gerenciar a autenticação de usuários.")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Realiza login de usuários", description = "Método para realizar o login de usuários utilizando JWT.")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso!")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    public ResponseEntity<TokenJWTDTO> efetuarLogin(@RequestBody @Valid AutenticacaoDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var token = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenJWTDTO(token));
    }

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Cadastra um novo usuário no fórum", description = "Método para cadastrar um novo usuário no fórum, com validação para evitar e-mails duplicados.")
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!")
    @ApiResponse(responseCode = "400", description = "E-mail já cadastrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor!")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid CadastroDTO dados) {
        if (this.usuarioRepository.findByEmail(dados.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = passwordEncoder.encode(dados.senha());
        Usuario usuario = new Usuario(dados.nome(), dados.email(), encryptedPassword);
        this.usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}