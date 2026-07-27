package com.paccanaro.gateway.pagamento.service;

import com.paccanaro.gateway.pagamento.model.Usuario;
import com.paccanaro.gateway.pagamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        //  Deixa o Spring fazer o trabalho pesado: chamar o Google,
        // validar o token e montar o objeto OAuth2User padrão
        OidcUser oidcUser = super.loadUser(userRequest);

        //  Extrai os dados que vieram do Google
        String email = oidcUser.getAttribute("email");
        String nome = oidcUser.getAttribute("name");

        // Nossa lógica de negócio: "find or create"
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() ->{
                    Usuario novo = new Usuario();
                    novo.setEmail(email);
                    novo.setNome(nome);
                    return novo;
                });


        // Atualiza o nome (caso o usuário já existisse, mas tenha mudado o nome no Google)
        usuario.setNome(nome);
        usuarioRepository.save(usuario);

        // Devolve o OAuth2User original para o Spring Security continuar o fluxo normalmente
        return  oidcUser;
    }


}
