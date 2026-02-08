package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
				Department dep = new  Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				//instancia o vendedor a partir do resultado da consulta, usando os dados retornados pelo ResultSet e o departamento instanciado anteriormente
				Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep); //associa o departamento ao vendedor, associa os objetos
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

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
