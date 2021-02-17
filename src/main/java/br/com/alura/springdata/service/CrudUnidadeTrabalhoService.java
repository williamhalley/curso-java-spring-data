package br.com.alura.springdata.service;

import java.util.Arrays;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import br.com.alura.springdata.orm.Funcionario;
import br.com.alura.springdata.orm.UnidadeTrabalho;
import br.com.alura.springdata.repository.FuncionarioRepository;
import br.com.alura.springdata.repository.UnidadeTrabalhoRepository;
import javassist.NotFoundException;


@Service
public class CrudUnidadeTrabalhoService {	
	
	//@Autowired  //ao invés do Autowired foi inicialiado pelo construtor
	private UnidadeTrabalhoRepository unidadeTrabalhoRepository;
	//@Autowired  //ao invés do Autowired foi inicialiado pelo construtor
	private FuncionarioRepository funcionarioRepository;
	
	private Boolean systemBoolean = true;
	
	public CrudUnidadeTrabalhoService(UnidadeTrabalhoRepository unidadeTrabalhoRepository, 
			FuncionarioRepository funcionarioRepository) {
		this.unidadeTrabalhoRepository = unidadeTrabalhoRepository;
		this.funcionarioRepository = funcionarioRepository;
	}
	
	
	public void inicial(Scanner scanner) {
				
		while(systemBoolean) {
			
			System.out.println("Qual ação você deseja executar em Unidade de Trabalho?");
			System.out.println("0 - Sair\n" + 
							   "1 - Mostrar todos as Unidades de Trabalho\n" + 
							   "2 - Buscar Unidade de Trabalho por id\n" + 
							   "3 - Criar Unidade de Trabalho\n" +
							   "4 - Atualizar Unidade de Trabalho\n" + 
							   "5 - Deletar Unidade de Trabalho");
			
			int action = scanner.nextInt();
			scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
			
			switch (action) {
			case 0:
				systemBoolean = false;
				break;
			case 1:
				getUnidadesDeTrabalho();;
				break;
			case 2:
				try {
					getUnidadeDeTrabalhoById(scanner);
				} catch (NotFoundException e) {
					e.printStackTrace();
					System.out.println("Não existe Unidade de Trabalho com este id");
				}
				break;
			case 3: 
				create(scanner);
				break;
			case 4:
				try {
					update(scanner);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (NotFoundException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				break;
			case 5:
				delete(scanner);
				break;
			default:
				break;
			}
		}		
	}


	private void getUnidadesDeTrabalho() {		
		Iterable<UnidadeTrabalho> listaUnidadesTrabalho = unidadeTrabalhoRepository.findAll();		
		listaUnidadesTrabalho.forEach(u -> System.out.println(u));
	}
	
	private void getUnidadeDeTrabalhoById(Scanner scanner) throws NotFoundException {
		
		UnidadeTrabalho unidadeTrabalho = unidadeTrabalhoRepository.findById(scanner.nextInt())
				.orElseThrow(() -> new NotFoundException("Não existe Unidade de Trabalho com este id"));
		
		scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
		
		System.out.println(unidadeTrabalho);
	}	
	
	private void create(Scanner scanner) {
		
		String[] arrayScanner = scanner.nextLine().split(";");
		
		UnidadeTrabalho unidadeTrabalho = new UnidadeTrabalho();		
		
		/*
		Funcionario funcionario = new Funcionario();		
		Cargo cargo = cargoRepository.findById(1).orElseThrow(() -> new NotFoundException("Cargo não encontrado"));
		
		funcionario.setCargo(cargo);
		funcionario.setCpf("352870");
		funcionario.setDataContracao(LocalDate.now());
		funcionario.setNome("William Halley");
		funcionario.setSalario(new BigDecimal(7000));
		funcionarioRepository.save(funcionario);
		*/		
		
		Funcionario funcionario = funcionarioRepository.findById(Integer.parseInt(arrayScanner[2])).get();
		
		unidadeTrabalho.setDescricao(arrayScanner[0]);
		unidadeTrabalho.setEndereço(arrayScanner[1]);
		unidadeTrabalho.setFuncionarios(Arrays.asList(funcionario));
		unidadeTrabalhoRepository.save(unidadeTrabalho);
	}	
	
	private void update(Scanner scanner) throws NumberFormatException, NotFoundException {
		
		String[] arrayScanner = scanner.nextLine().split(";");
		
		UnidadeTrabalho unidadeTrabalho = unidadeTrabalhoRepository.findById(Integer.parseInt(arrayScanner[0]))
				.orElseThrow(() -> new NotFoundException("Não encontrado"));
		
		unidadeTrabalho.setDescricao(arrayScanner[1]);
		unidadeTrabalho.setEndereço(arrayScanner[2]);
		//unidadeTrabalho.setFuncionarios(Arrays.asList(funcionario));
		unidadeTrabalhoRepository.save(unidadeTrabalho);
	}
	
	private void delete(Scanner scanner) {
		
		int id = scanner.nextInt();	
		scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
		
		unidadeTrabalhoRepository.deleteById(id);
		System.out.println("Excluído com sucesso");		
	}

}
