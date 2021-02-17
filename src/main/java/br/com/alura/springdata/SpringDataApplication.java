package br.com.alura.springdata;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.springdata.service.CrudCargoService;
import br.com.alura.springdata.service.CrudFuncionarioService;
import br.com.alura.springdata.service.CrudUnidadeTrabalhoService;
import br.com.alura.springdata.service.RelatorioFuncionarioDinamico;
import br.com.alura.springdata.service.RelatorioService;


@SpringBootApplication
public class SpringDataApplication implements CommandLineRunner {
	
	private Boolean systemBoolean = true;
	private final CrudCargoService cargoService;	
	private final CrudUnidadeTrabalhoService unidadeTrabalhoService;
	private final CrudFuncionarioService funcionarioService;
	private final RelatorioService relatorioService;
	private final RelatorioFuncionarioDinamico relatorioFuncionarioDinamico;
	
	public SpringDataApplication(CrudCargoService cargoService, 
			CrudUnidadeTrabalhoService unidadeTrabalhoService,
			CrudFuncionarioService funcionarioService,
			RelatorioService relatorioService,
			RelatorioFuncionarioDinamico relatorioFuncionarioDinamico) {
		this.cargoService = cargoService;
		this.unidadeTrabalhoService = unidadeTrabalhoService;
		this.funcionarioService = funcionarioService;
		this.relatorioService = relatorioService;
		this.relatorioFuncionarioDinamico = relatorioFuncionarioDinamico;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringDataApplication.class, args);
	}

	
	// os 3 pontinhos se chama varargs (significa um número variável de argumentos) 
	// https://www.guj.com.br/t/duvida-sobre-tres-pontos-na-assinatura-do-metodo/132138/2
	@Override
	public void run(String... args) throws Exception {
		
		Scanner scanner = new Scanner(System.in);
		
		while(systemBoolean) {
			
			System.out.println("Qual assunto você deseja executar?");
			System.out.println("0 - Sair\n" + 
							   "1 - Funcionário\n" + 
							   "2 - Cargo\n" +
							   "3 - Unidade de Trabalho\n" + 
							   "4 - Relatórios\n" + 
							   "5 - Relatório Dinâmico de Funcionário(Especificação)");
			
			int action = scanner.nextInt();
			scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
			
			switch (action) {
			case 0:
				systemBoolean = false;
				break;
			case 1:
				funcionarioService.inicial(scanner);
				break;
			case 2:
				cargoService.inicial(scanner);
				break;
			case 3:
				unidadeTrabalhoService.inicial(scanner);
				break;
			case 4:
				relatorioService.inicial(scanner);
				break;
			case 5:
				relatorioFuncionarioDinamico.inicial(scanner);
				break;
			default:
				break;
			}			
		}
	}

}
