package model.dao;

import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class DaoFactory {
	
	// terá operações 4estaticas
	//deixa somente a interface e nao expoe o esqueleto
	public static Seller  createSellerDao() {
		
		return new SellerDaoJDBC();
	}

}
