package com.generation.blogpessoal.service;


import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.UserLogin;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Optional<Usuario>cadastrarUsuario(Usuario usuario) {
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
		return Optional.empty();
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		return Optional.of(usuarioRepository.save(usuario));
	}
	
	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);

	}
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaBanco);

	}

	public Optional<UserLogin> logarUsuario(Optional<UserLogin> userLogin) {

		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userLogin.get().getUsuario());

		if (usuario.isPresent()) {
			if(compararSenhas(userLogin.get().getSenha(), usuario.get().getSenha())) {
				userLogin.get().setId(usuario.get().getId());
				userLogin.get().setNome(usuario.get().getNome());
				userLogin.get().setToken(gerarBasicToken(userLogin.get().getUsuario(), userLogin.get().getSenha()));
				userLogin.get().setSenha(usuario.get().getSenha());
			
				
				return userLogin;
			}
			
		}
		return Optional.empty();
	}

	
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}

public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			
			/**
		 	* Se o Usuário existir no Banco de Dados, a senha será criptografada
		 	* através do Método criptografarSenha.
		 	*/
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			/**
		 	* Assim como na Expressão Lambda, o resultado do método save será retornado dentro
		 	* de um Optional, com o Usuario persistido no Banco de Dados ou um Optional vazio,
			* caso aconteça algum erro.
			* 
			* ofNullable​ -> Se um valor estiver presente, retorna um Optional com o valor, 
			* caso contrário, retorna um Optional vazio.
		 	*/
			return Optional.ofNullable(usuarioRepository.save(usuario));
			
		}
		
		/**
		 * empty -> Retorna uma instância de Optional vazia, caso o usuário não seja encontrado.
		 */
		return Optional.empty();
	
	}	


			

}
