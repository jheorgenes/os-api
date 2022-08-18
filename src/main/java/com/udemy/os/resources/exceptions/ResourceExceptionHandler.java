package com.udemy.os.resources.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.udemy.os.services.exceptions.DataIntegratyViolationException;
import com.udemy.os.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class) //Intercepta a Excessão lançada do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException e){
		StandardError error = new StandardError(
			System.currentTimeMillis(), //Pegando o timestamps
			HttpStatus.NOT_FOUND.value(), //Passando o Status Code 404
			e.getMessage() //Obtendo a mensagem do objeto 
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); //Retorna o status code da requisição e o StandardError construído
	}
	
	@ExceptionHandler(DataIntegratyViolationException.class) 
	public ResponseEntity<StandardError> objectNotFoundException(DataIntegratyViolationException e){ //Mesmo nome de método para convenção das Exceptions Interceptadas
		StandardError error = new StandardError(
			System.currentTimeMillis(), 
			HttpStatus.BAD_REQUEST.value(), //Status code 400
			e.getMessage()  
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public ResponseEntity<StandardError> objectNotFoundException(MethodArgumentNotValidException e){ //Mesmo nome de método para convenção das Exceptions Interceptadas
		ValidationError error = new ValidationError(
			System.currentTimeMillis(), 
			HttpStatus.BAD_REQUEST.value(), 
			"Erro na validação dos campos!"
		);
		for(FieldError x : e.getBindingResult().getFieldErrors()) { //Transforma a lista de erros capturados em um objeto FieldError
			error.addError(x.getField(), x.getDefaultMessage()); //Adiciona o ValidadionError na lista
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
	}
}
