package com.udemy.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.os.domain.Pessoa;
import com.udemy.os.domain.Tecnico;
import com.udemy.os.dtos.TecnicoDTO;
import com.udemy.os.repositories.PessoaRepository;
import com.udemy.os.repositories.TecnicoRepository;
import com.udemy.os.services.exceptions.DataIntegratyViolationException;
import com.udemy.os.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + ", Tipo: " + Tecnico.class.getName()));
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}
	
	public Tecnico create(TecnicoDTO objDTO) {
		if(findByCPF(objDTO) != null) { //Se houver cpf igual no banco
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		return tecnicoRepository.save(new Tecnico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
	}
	
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		Tecnico oldObj = findById(id); //Valida se já existe ID
		if(findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) { //Se CPF não for válido ou for diferente do CPF do cadastro antigo
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		/* Atualiza o objeto */
		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		return tecnicoRepository.save(oldObj); //Salva no banco de dados
	}
	
	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if(obj.getList().size() > 0) { //Se o Tecnico tiver alguma ordem de serviço adicionada
			throw new DataIntegratyViolationException("Pessoa possui Ordens de Serviço, não pode ser deletado!");
		}
		tecnicoRepository.deleteById(id);
	}
	
	private Pessoa findByCPF(TecnicoDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());
		if(obj != null) {
			return obj; //Encontrou um cpf igual no banco
		}
		return null; //Se não encontrou cpf igual, retorna null
	}
}
