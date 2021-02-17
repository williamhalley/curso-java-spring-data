package br.com.alura.springdata.orm;

import java.math.BigDecimal;

public interface FuncionarioProjecao {
	
	//para o Spring Data reconhecer precisa inicicar com get+nome do atributo definido na entidade Funcionario
	Integer getId();
	String getNome();
	BigDecimal getSalario();	

}
