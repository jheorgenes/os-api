package com.udemy.os.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.os.domain.Cliente;
import com.udemy.os.domain.OS;
import com.udemy.os.domain.Tecnico;
import com.udemy.os.domain.enuns.Prioridade;
import com.udemy.os.domain.enuns.Status;
import com.udemy.os.dtos.OSDTO;
import com.udemy.os.repositories.OSRepository;
import com.udemy.os.services.exceptions.ObjectNotFoundException;

@Service
public class OsService {

	@Autowired
	private OSRepository osRepository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public OS findById(Integer id) {
		Optional<OS> obj = osRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id + ", Tipo: " + OS.class.getName()));
	}
	
	public List<OS> findAll(){
		return osRepository.findAll();
	}

	public OS create(@Valid OSDTO objDTO) {
		return fromDTO(objDTO);
	}
	
	public OS update(@Valid OSDTO objDTO) {
		findById(objDTO.getId());
		return fromDTO(objDTO);
	}
	
	private OS fromDTO(OSDTO objDTO) {
		OS newObj = new OS();
		newObj.setId(objDTO.getId());
		newObj.setObservacoes(objDTO.getObservacoes());
		newObj.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade())); //Pega o Integer do DTO e chama o Método estático para fazer a conversão para o Enum
		newObj.setStatus(Status.toEnum(objDTO.getStatus()));
		
		Tecnico tec = tecnicoService.findById(objDTO.getTecnico()); //Buscando no banco pelo ID obtido do TecnicoDTO
		Cliente cli = clienteService.findById(objDTO.getCliente()); //Buscando no banco pelo ID obtido do ClienteDTO
		
		newObj.setTecnico(tec);
		newObj.setCliente(cli);
		
		if(newObj.getStatus().getCod().equals(2)) {
			newObj.setDataFechamento(LocalDateTime.now());
		}
		
		return osRepository.save(newObj);
	}
}
