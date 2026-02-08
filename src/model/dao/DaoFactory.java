package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class DaoFactory {
	
	// terá operações 4estaticas
	//deixa somente a interface e nao expoe o esqueleto
	public static SellerDao  createSellerDao () {
		
		return new SellerDaoJDBC(DB.getConnection());
	}

}
