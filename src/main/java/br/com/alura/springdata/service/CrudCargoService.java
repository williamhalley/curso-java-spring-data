package br.com.alura.springdata.service;

import java.util.Optional;
import java.util.Scanner;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import br.com.alura.springdata.orm.Cargo;
import br.com.alura.springdata.repository.CargoRepository;
import javassist.NotFoundException;


@Service
public class CrudCargoService {
	
	//@Autowired	
	private final CargoRepository cargoRepository;
	private Boolean systemBoolean = true;
	
	public CrudCargoService(CargoRepository cargoRepository) {
		this.cargoRepository = cargoRepository;
	}
	
	public void inicial(Scanner scanner) {
		
		while(systemBoolean) {			
			
			System.out.println("Qual ação você deseja executar em Funcionario?");
			System.out.println("0 - Sair\n" + 
							   "1 - Mostrar todos os cargos\n" + 
							   "2 - Buscar cargo por id\n" + 
							   "3 - Criar cargo\n" +
							   "4 - Atualizar cargo\n" + 
							   "5 - Deletar cargo");
			
			int action = scanner.nextInt();
			scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
			
			switch (action) {
			case 0:
				systemBoolean = false;
				break;
			case 1:
				getCargos();
				break;
			case 2:
				getCargosById(scanner);
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
	
	
	private void getCargos() {
		
		//List<Cargo> listaCargos = (List<Cargo>)cargoRepository.findAll();
		//listaCargos.forEach(c -> System.out.println(c.getDescricao()));
		
		Iterable<Cargo> cargosIterable = cargoRepository.findAll();
		cargosIterable.forEach(c -> System.out.println(c.getDescricao())); //ou sobrescrever o toString de Cargo
	}
	
	private void getCargosById(Scanner scanner) {
		
		int id = scanner.nextInt();
		scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
		
		Cargo cargo = cargoRepository.findById(id).get();		
		System.out.println(cargo.getDescricao());
	}	
	
	private void create(Scanner scanner) {
		
		System.out.println("Descrição do Cargo");
		
		Cargo cargo = new Cargo();
		
		String descricao = scanner.nextLine(); 
		cargo.setDescricao(descricao);
		
		cargoRepository.save(cargo);	
		System.out.println("Cargo Salvo");
	}
	
	private void update(Scanner scanner) {
		
		String linha = scanner.nextLine();
		String[] array = linha.split(";");
		
		//Cargo cargo = cargoRepository.findById(Integer.parseInt(array[0])).get();
		Optional<Cargo> optionalCargo = cargoRepository.findById(Integer.parseInt(array[0]));		
		
		try {
			Cargo cargo;
			cargo = optionalCargo.orElseThrow(() -> new NotFoundException("Não encontrado"));
			cargo.setDescricao(array[1]);
			cargoRepository.save(cargo);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void delete(Scanner scanner) {
		
		try {
			int idCargo = scanner.nextInt();	
			scanner.nextLine(); //somente o nextLine() que consome o \n ao teclar enter da linha acima -> nextInt()
			
			cargoRepository.deleteById(idCargo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			//e.getMessage();
			//throw new Exception(e); 
		}
	}

}
