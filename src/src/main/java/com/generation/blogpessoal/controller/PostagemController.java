package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostagemController {
	
		@Autowired
		private PostagemRepository repository;
		
		@GetMapping
		public ResponseEntity<List<Postagem>> GetAll(){
			return ResponseEntity.ok(repository.findAll());
			
		}
		
		@GetMapping("/{id}")
			public ResponseEntity<Postagem>GetById(@PathVariable Long id){
				return repository.findById(id)
						.map(resp -> ResponseEntity.ok(resp))
						.orElse(ResponseEntity.notFound().build());
		}
		
		@GetMapping("/titulo/{titulo}")
		public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo){
			return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
		}
		
		@PostMapping
		public ResponseEntity<Postagem> postPostagem (@Valid @RequestBody Postagem postagem){

			if (repository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();	
		}
		
		@PutMapping
		public ResponseEntity<Postagem> putPostagem (@Valid @RequestBody Postagem postagem){
			
				if (repository.existsById(postagem.getId())){
				
				if (repository.existsById(postagem.getTema().getId()))
					return ResponseEntity.status(HttpStatus.OK)
							.body(repository.save(postagem));}
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}		
				
		
		@DeleteMapping("/{id}")
		public void delete (@PathVariable Long id ){
			repository.deleteById(id);
		}
}
