package br.com.alura.springdata.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.alura.springdata.orm.Funcionario;
import br.com.alura.springdata.orm.FuncionarioProjecao;
import br.com.alura.springdata.repository.FuncionarioRepository;


@Service
public class RelatorioService {
	
	private final FuncionarioRepository funcionarioRepository;
	private Boolean systemBoolean = true;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public RelatorioService(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}
	
	
	public void inicial(Scanner scanner) {
		
		while(systemBoolean) {
			
			System.out.println("Qual ação você deseja executar em Relatório?");
			System.out.println("0 - Sair\n" + 
							   "1 - Buscar funcionários por nome\n" + 
							   "2 - Buscar funcionários por nome/salaiorMaiorQue/dataContratacao\n" +
							   "3 - Buscar funcionários por data de contratação\n" + 
							   "4 - Buscar funcionário por salário");
			
			int action = scanner.nextInt();
			scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()			
			
			switch (action) {
			case 0:
				systemBoolean = false;
				break;
			case 1:
				getFuncionarioByNome(scanner);
				break;
			case 2:
				findNomeSalarioMaiorDataContratacao(scanner);
				break;
			case 3:
				getFuncionariosDataContratacao(scanner);
				break;
			case 4:
				pesquisaFuncionarioSalario();
				break;
			default:
				break;
			}
		}		
	}
	
	
	private void getFuncionarioByNome(Scanner scanner) {
		
		System.out.println("Informe o nome que deseja pesquisar + página que deseja visualizar:");
		String[] scannerArray = scanner.nextLine().split(";");
		String nome = scannerArray[0];
		int pageNumber = Integer.parseInt(scannerArray[1]);
		
		Pageable pageable = PageRequest.of(pageNumber, 5, Sort.unsorted());
		
		List<Funcionario> funcionarios = funcionarioRepository.findByNome(nome, pageable);		
		Page<Funcionario> pageFuncionarios = new PageImpl<>(funcionarios);
		
		System.out.println(pageFuncionarios);
		System.out.println("Página atual: " + pageFuncionarios.getNumber());
		System.out.println("Total elementos: " + pageFuncionarios.getTotalElements());
		
		pageFuncionarios.forEach(funcionario -> System.out.println(funcionario));
	}
	
	private void findNomeSalarioMaiorDataContratacao(Scanner scanner) {
		
		// nome;salario;dataContratacao
		String[] scannerArray = scanner.nextLine().split(";");
		
		String nome = scannerArray[0];
		BigDecimal salario = new BigDecimal(scannerArray[1]);
		
		String dataString = scannerArray[2];
		
		LocalDate data = LocalDate.parse(dataString, formatter);
		
		List<Funcionario> funcionarios = funcionarioRepository.findNomeSalarioMaiorDataContratacao(nome, salario, data);
		
		funcionarios.forEach(f -> System.out.println(f.getNome() + " | " + f.getSalario()));
	}
	
	private void getFuncionariosDataContratacao(Scanner scanner) {
		
		System.out.println("Informe a data de contratação:");
		String dataString = scanner.nextLine();
		
		LocalDate data = LocalDate.parse(dataString, formatter);
		List<Funcionario> funcionarios = funcionarioRepository.findDataContratacaoMaior(data);
		
		funcionarios.forEach(f -> System.out.println(f.getNome() + " | " + f.getSalario()));
	}
	
	private void pesquisaFuncionarioSalario() {
		
		List<FuncionarioProjecao> funcionariosProjecao = funcionarioRepository.findFuncionarioSalario();
		
		funcionariosProjecao.forEach(f -> System.out.println(f.getId() + " | " +
															 f.getNome() + " | " + 
															 f.getSalario()));
	}

}
