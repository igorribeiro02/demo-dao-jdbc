package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn; // este metodo é um construtor, ele recebe a conexao com o banco de dados e armazena em um atributo do tipo Connection para ser usado nos outros metodos da classe
	} // par anao termos que fazermos mais, podemos usar em todoo o jdbc
	

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;

		ResultSet rs = null;
		// a conexão com o banco será uma depenedencia do dao com o banco 
		try{
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName\n" + 
										"FROM seller INNER JOIN department\n" + 
										"ON seller.DepartmentId = department.Id\n" + 
										"WHERE seller.Id = ?");
			st.setInt(1, id); //recebe o id como parametro do metodo e seta no lugar do ? da consulta sql
			rs = st.executeQuery(); //executa a consulta e armazena o resultado em um objeto do tipo ResultSet

			//os objetos serão instanciados a partir do resultado da consulta, usando os dados retornados pelo ResultSet
			if(rs.next()){ // testa se veio algum resultado
				//instancia o departamento a partir do resultado da consulta, usando os dados retornados pelo ResultSet
				Department dep = instantiateDepartment(rs); //instancia o departamento a partir do resultado da consulta, usando os dados retornados pelo ResultSet
				//instancia o vendedor a partir do resultado da consulta, usando os dados retornados pelo ResultSet e o departamento instanciado anteriormente
				Seller obj = instantiateSeller(rs, dep); //associa o departamento ao vendedor, associa os objetos
				return obj; //retorna o vendedor instanciado por id
			}
			return null;
		} catch (SQLException e){
			throw new DbException(e.getMessage());
		} finally{
			DB.closeStatement(st);
			DB.closeResultSet(rs); 
		}

	}

	// cria um metodo auxiliar para instanciar o departamento a partir do resultado da consulta, usando os dados retornados pelo ResultSet
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	//cria um metodo auxiliar para instanciar o vendedor a partir do resultado da consulta, usando os dados retornados pelo ResultSet e o departamento instanciado anteriormente
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}


	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;

		ResultSet rs = null;
		// a conexão com o banco será uma depenedencia do dao com o banco 
		try{
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName\n" + 
										"FROM seller INNER JOIN department\n" + 
										"ON seller.DepartmentId = department.Id\n" + 
										
										+"ORDER BY Name");
			
			rs = st.executeQuery(); //executa a consulta e armazena o resultado em um objeto do tipo ResultSet

			List<Seller> list = new ArrayList<>(); //cria uma lista para armazenar os vendedores encontrados na consulta
			Map<Integer, Department> map = new HashMap<>(); //cria um mapa para armazenar os departamentos ja instanciados, onde a chave é o id do departamento e o valor é o objeto departamento instanciado

			//os objetos serão instanciados a partir do resultado da consulta, usando os dados retornados pelo ResultSet
			while(rs.next()){ // testa se veio algum resultado
				//testar se o departmento ja existe
				Department dep= map.get(rs.getInt("DepartmentId")); //pega o id do departamento da consulta e verifica se ja existe um departamento com esse id no mapa
				// isso ajuda a diferenciar os departamentos ja instanciados, evitando a criacao de varios objetos departamento para o mesmo departamento, o que pode causar problemas de desempenho e consumo de memoria, alem de dificultar a comparacao de objetos departamento, pois mesmo que sejam o mesmo departamento, eles serao objetos diferentes na memoria
				if( dep == null ){
					//instancia o departamento a partir do resultado da consulta, usando os dados retornados pelo ResultSet
				 dep = instantiateDepartment(rs); //instancia o departamento a partir da consulta, usando os dados retornados pelo ResultSet
				 map.put(rs.getInt("DepartmentId"), dep); //adiciona o departamento instanciado no mapa, associando o id do departamento ao objeto departamento
				}
				//instancia o vendedor a partir do resultado da consulta, usando os dados retornados pelo ResultSet e o departamento instanciado anteriormente
				Seller obj = instantiateSeller(rs, dep); //associa o departamento ao vendedor, associa os objetos
				// meu resultao pode ter 0 ou mais vlaores
				list.add(obj);
				} //adiciona o vendedor instanciado na lista de vendedores encontrados na consulta
			}
			return list; // porem desta forma eu crio outro departamento para cada vendedor, mesmo que seja o mesmo departamento, entao preciso criar um mapa para armazenar os departamentos ja instanciados e associar os vendedores a estes departamentos
			              // para evitar a criacao de varios objetos departamento para o mesmo departamento, o que pode causar problemas de desempenho e consumo de memoria, alem de dificultar a comparacao de objetos departamento, pois mesmo que sejam o mesmo departamento, eles serao objetos diferentes na memoria	
		} catch (SQLException e){
			throw new DbException(e.getMessage());
		} finally{
			DB.closeStatement(st);
			DB.closeResultSet(rs); 
		}
	}


	@Override
	public List<Seller> findByDepartment(Department department) {
		// objs de statement e resultset
		PreparedStatement st = null;

		ResultSet rs = null;
		// a conexão com o banco será uma depenedencia do dao com o banco 
		try{
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName\n" + 
										"FROM seller INNER JOIN department\n" + 
										"ON seller.DepartmentId = department.Id\n" + 
										"WHERE DepartmentId = ?"
										+"ORDER BY Name");
			st.setInt(1, department.getId()); //recebe o id como parametro do metodo e seta no lugar do ? da consulta sql
			rs = st.executeQuery(); //executa a consulta e armazena o resultado em um objeto do tipo ResultSet

			List<Seller> list = new ArrayList<>(); //cria uma lista para armazenar os vendedores encontrados na consulta
			Map<Integer, Department> map = new HashMap<>(); //cria um mapa para armazenar os departamentos ja instanciados, onde a chave é o id do departamento e o valor é o objeto departamento instanciado

			//os objetos serão instanciados a partir do resultado da consulta, usando os dados retornados pelo ResultSet
			while(rs.next()){ // testa se veio algum resultado
				//testar se o departmento ja existe
				Department dep= map.get(rs.getInt("DepartmentId")); //pega o id do departamento da consulta e verifica se ja existe um departamento com esse id no mapa
				// isso ajuda a diferenciar os departamentos ja instanciados, evitando a criacao de varios objetos departamento para o mesmo departamento, o que pode causar problemas de desempenho e consumo de memoria, alem de dificultar a comparacao de objetos departamento, pois mesmo que sejam o mesmo departamento, eles serao objetos diferentes na memoria
				if( dep == null ){
					//instancia o departamento a partir do resultado da consulta, usando os dados retornados pelo ResultSet
				 dep = instantiateDepartment(rs); //instancia o departamento a partir da consulta, usando os dados retornados pelo ResultSet
				 map.put(rs.getInt("DepartmentId"), dep); //adiciona o departamento instanciado no mapa, associando o id do departamento ao objeto departamento
				}
				//instancia o vendedor a partir do resultado da consulta, usando os dados retornados pelo ResultSet e o departamento instanciado anteriormente
				Seller obj = instantiateSeller(rs, dep); //associa o departamento ao vendedor, associa os objetos
				// meu resultao pode ter 0 ou mais vlaores
				list.add(obj);
				} //adiciona o vendedor instanciado na lista de vendedores encontrados na consulta
			}
			return list; // porem desta forma eu crio outro departamento para cada vendedor, mesmo que seja o mesmo departamento, entao preciso criar um mapa para armazenar os departamentos ja instanciados e associar os vendedores a estes departamentos
			              // para evitar a criacao de varios objetos departamento para o mesmo departamento, o que pode causar problemas de desempenho e consumo de memoria, alem de dificultar a comparacao de objetos departamento, pois mesmo que sejam o mesmo departamento, eles serao objetos diferentes na memoria	
		} catch (SQLException e){
			throw new DbException(e.getMessage());
		} finally{
			DB.closeStatement(st);
			DB.closeResultSet(rs); 
		}

	}
}	
