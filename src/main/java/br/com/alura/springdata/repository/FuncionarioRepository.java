package br.com.alura.springdata.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.springdata.orm.Funcionario;
import br.com.alura.springdata.orm.FuncionarioProjecao;


@Repository
//PaginAndSortingRepository trabalha com paginação. Estende de CrudRepository
//JpaSpecificationExecutor --> responsável por executar as especificações como query dinâmicas
public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Integer>,
			JpaSpecificationExecutor<Funcionario> {

//CrudRepository  
// public interface FuncionarioRepository extends CrudRepository<Funcionario, Integer>  {

//JpaRepository é ideal para trabalhar com arquivos em lotes. Ex: apagar várias arquivos de uma vez
//public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer>  {	
	
	
	// se chama Derived Query (o próprio nome do método monta a consulta
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
	List<Funcionario> findByNome(String nome, Pageable pageable);
	
	//Query JPQL
	//List<Funcionario> findByNomeAndSalarioGreaterThanAndDataContratacao(String nome, BigDecimal salario,  LocalDate dataContratacao);
	@Query("select f from Funcionario f "
			+ "where f.nome = :nome and f.salario >= :salario and f.dataContratacao = :dataContratacao")
	List<Funcionario> findNomeSalarioMaiorDataContratacao(String nome, BigDecimal salario, LocalDate dataContratacao);
	
	// List<Funcionario> findByCargoDescricao(String descricao);
	@Query("SELECT f FROM Funcionario f JOIN f.cargo c WHERE c.descricao = :descricao")
	List<Funcionario> findByCargoPelaDescricao(String descricao);
	
	//Query Nativa
	@Query(value = "select * from funcionarios f where f.data_contratacao >= :data", nativeQuery = true)
	List<Funcionario> findDataContratacaoMaior(LocalDate data);

	// Interface based Projection --> FuncionarioProjecao é uma interface para projecao
	// class-based projections --> Como alternativa pode criar uma classe FuncionarioDto contendo os atributos do retorno da query. Porém é necessário criar getter e setter + construtor recebendo os atributos na ordem da query
	// Dto --> Data Transfer Object
	// Precisa ser uma query nativa
	@Query(value = "select f.id, f.nome, f.salario from funcionarios f order by f.salario desc", nativeQuery = true)
	List<FuncionarioProjecao> findFuncionarioSalario( );
	
	
}
