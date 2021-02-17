package br.com.alura.springdata.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.stereotype.Service;

import br.com.alura.springdata.orm.Funcionario;
import br.com.alura.springdata.repository.FuncionarioRepository;
import br.com.alura.springdata.specification.SpecificationFuncionario;

@Service
public class RelatorioFuncionarioDinamico {
	
	private final FuncionarioRepository funcionarioRepository;
	private Boolean systemBoolean = true;
	
	public RelatorioFuncionarioDinamico(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}
	
	public void inicial(Scanner scanner) {
		
		while(systemBoolean) {
			
			System.out.println("0 - Sair\n" + 
							   "1 - Buscar Funcionario por Nome\n" +
							   "2 - Buscar Funcionário por Query Dinâmica (nome;cpf;salario;dataContratacao)");
			
			int action = scanner.nextInt();
			scanner.nextLine();
			
			switch (action) {
			case 0:
				systemBoolean = false;
				break;
			case 1:
				findFuncionariosPorNome(scanner);
				break;
			case 2:
				findFuncionarioQueryDinamica(scanner);
				break;
			default:
				break;
			}			
		}
	}
	
	private void findFuncionarioQueryDinamica(Scanner scanner) {

		//nome;cpf;salario;dataContratacao
		String[] scannerArray = scanner.nextLine().split(";");
		
		String nome = null;
		String cpf = null;
		BigDecimal salario = null;
		LocalDate dataContratacao = null;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		if (!scannerArray[0].equalsIgnoreCase("NULL")){
			nome = scannerArray[0];
		}
		
		if (!scannerArray[1].equalsIgnoreCase("NULL")){
			cpf = scannerArray[1];
		}
		
		if (!scannerArray[2].equalsIgnoreCase("NULL")){
			salario = new BigDecimal(scannerArray[2]);
		}
		
		if (!scannerArray[3].equalsIgnoreCase("NULL")){
			dataContratacao = LocalDate.parse(scannerArray[3], formatter);
		}		
		
		/*
		String nome = scannerArray[0];
		String cpf = scannerArray[1];
		BigDecimal salario = new BigDecimal(scannerArray[2]);
		LocalDate dataContratacao = LocalDate.parse(scannerArray[3], formatter);
		*/
		
		List<Funcionario> funcionarios = funcionarioRepository.findAll(Specification
				.where(
						SpecificationFuncionario.nome(nome))
						.or(SpecificationFuncionario.cpf(cpf))
						.or(SpecificationFuncionario.salario(salario))
						.or(SpecificationFuncionario.dataContratacao(dataContratacao)));
		
		funcionarios.forEach(System.out::println);
	}

	private void findFuncionariosPorNome(Scanner scanner) {
		
		System.out.println("Digite o nome do funcionário que deseja procurar: ");
		String nome = scanner.nextLine();
		
		List<Funcionario> funcionarios = funcionarioRepository.findAll(SpecificationFuncionario.nome(nome));

		funcionarios.forEach(System.out::println);
	}

}
