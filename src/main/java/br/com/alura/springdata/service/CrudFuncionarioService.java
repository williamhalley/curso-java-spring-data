package br.com.alura.springdata.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.alura.springdata.orm.Cargo;
import br.com.alura.springdata.orm.Funcionario;
import br.com.alura.springdata.orm.UnidadeTrabalho;
import br.com.alura.springdata.repository.CargoRepository;
import br.com.alura.springdata.repository.FuncionarioRepository;
import br.com.alura.springdata.repository.UnidadeTrabalhoRepository;
import javassist.NotFoundException;


@Service
public class CrudFuncionarioService {
	
	private final FuncionarioRepository funcionarioRepository;
	private final CargoRepository cargoRepository;
	private final UnidadeTrabalhoRepository unidadeTrabalhoRepository;	
	private Boolean systemBoolean = true;	
	
	public CrudFuncionarioService(FuncionarioRepository funcionarioRepository,
			CargoRepository cargoRepository,
			UnidadeTrabalhoRepository unidadeTrabalhos) {
		this.funcionarioRepository = funcionarioRepository;
		this.cargoRepository = cargoRepository;
		this.unidadeTrabalhoRepository = unidadeTrabalhos;
	}
	
	public void inicial(Scanner scanner) {
		
		while(systemBoolean) {			
			
			System.out.println("Qual ação você deseja executar em Funcionario?");
			System.out.println("0 - Sair\n" + 
							   "1 - Mostrar todos os funcionários\n" + 
							   "2 - Buscar funcionário por id\n" + 
							   "3 - Criar funcionário\n" +
							   "4 - Atualizar funcionário\n" + 
							   "5 - Deletar funcionário");
			
			int action = scanner.nextInt();
			scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
			
			switch (action) {
			case 0:
				systemBoolean = false;
				break;
			case 1:
				getFuncionarios(scanner);
				break;
			case 2:
				try {
					getFuncionarioById(scanner);
				} catch (NotFoundException e) {				
					e.printStackTrace();
					System.out.println("Não existe funcionário com este id");
				}
				break;
			case 3: 
				create(scanner);
				break;
			case 4:
				update(scanner);
				break;
			case 5:
				delete(scanner);
				break;
			default:
				break;
			}
		}		
	}
	

	private void getFuncionarios(Scanner scanner) {
		
		System.out.println("Qual página você deseja visualizar?");
		int pageNumber = scanner.nextInt();
		
		// Pageable pageable = PageRequest.of(pageNumber, 5, Sort.unsorted());
		Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "salario", "nome")); // 2º e 3º parametros são por qual campo será ordenado
		
		Page<Funcionario> funcionarios = funcionarioRepository.findAll(pageable);
		
		System.out.println(funcionarios);
		System.out.println("Página atual: " + funcionarios.getNumber());
		System.out.println("Total elementos: " + funcionarios.getTotalElements());
		
		funcionarios.forEach(funcionario -> System.out.println(funcionario));
	}
	
	private void getFuncionarioById(Scanner scanner) throws NotFoundException {
				
		Funcionario funcionario = funcionarioRepository.findById(scanner.nextInt())
				.orElseThrow(() -> new NotFoundException("Não existe funcionário com este id"));
		
		scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
		
		System.out.println(funcionario.getNome() + " | " + funcionario.getCargo().getDescricao());
	}
	
	private void create(Scanner scanner) {
		
		// nome;cpf;salario;cargoId;unidadeTrabalhoId
		String[] dados = scanner.nextLine().split(";");
		
		String nome = dados[0];
		String cpf = dados[1];
		BigDecimal salario = new BigDecimal(dados[2]);
		int cargoId = Integer.parseInt(dados[3]);
		int unidadeTrabalhoId = Integer.parseInt(dados[4]);
		
		Cargo cargo = cargoRepository.findById(cargoId).get();
		UnidadeTrabalho unidadeTrabalho = unidadeTrabalhoRepository.findById(unidadeTrabalhoId).get();
				
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(nome);
		funcionario.setCpf(cpf);
		funcionario.setDataContratacao(LocalDate.now());
		funcionario.setSalario(salario);
		funcionario.setCargo(cargo);
		funcionario.setUnidadeTrabalhos(Arrays.asList(unidadeTrabalho));

		Funcionario novoFuncionario = funcionarioRepository.save(funcionario);
		
		System.out.println(novoFuncionario.getId() + " | " + novoFuncionario.getNome() + "\n");
	}
	
	private void update(Scanner scanner) {
		
				// nome;cpf;salario;cargoId;unidadeTrabalhoId;funcionarioId
				String[] dados = scanner.nextLine().split(";");
				
				String nome = dados[0];
				String cpf = dados[1];
				BigDecimal salario = new BigDecimal(dados[2]);
				int cargoId = Integer.parseInt(dados[3]);
				int unidadeTrabalhoId = Integer.parseInt(dados[4]);
				int funcionarioId = Integer.parseInt(dados[5]);
								
				Funcionario funcionario = funcionarioRepository.findById(funcionarioId).get();
				Cargo cargo = cargoRepository.findById(cargoId).get();
				UnidadeTrabalho unidadeTrabalho = unidadeTrabalhoRepository.findById(unidadeTrabalhoId).get();
						
				funcionario.setNome(nome);
				funcionario.setCpf(cpf);
				funcionario.setDataContratacao(LocalDate.now());
				funcionario.setSalario(salario);
				funcionario.setCargo(cargo);
				funcionario.setUnidadeTrabalhos(Arrays.asList(unidadeTrabalho));

				Funcionario novoFuncionario = funcionarioRepository.save(funcionario);
				
				System.out.println(novoFuncionario.getId() + " | " + novoFuncionario.getNome() + "\n");
	}
	
	private void delete(Scanner scanner) {
		
		int id = scanner.nextInt();
		scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
		
		funcionarioRepository.deleteById(id);
		System.out.println("Funcionário apagado com sucesso");
		
	}

}
