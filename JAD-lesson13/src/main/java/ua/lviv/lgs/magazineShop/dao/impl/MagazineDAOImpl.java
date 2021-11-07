package ua.lviv.lgs.magazineShop.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import ua.lviv.lgs.magazineShop.dao.DAOException;
import ua.lviv.lgs.magazineShop.dao.DAOFactory;
import ua.lviv.lgs.magazineShop.dao.MagazineDAO;
import ua.lviv.lgs.magazineShop.dao.UserDAO;
import ua.lviv.lgs.magazineShop.domain.Magazine;
import ua.lviv.lgs.magazineShop.domain.User;

public class MagazineDAOImpl implements MagazineDAO {
	private EntityManager em = DAOFactory.getEntityManager();
	
	private Logger log = Logger.getLogger(MagazineDAOImpl.class);

	@Override
	public Magazine insert(Magazine magazine) throws DAOException {
		log.info("Creating new magazine in database...");

		try {
			log.trace("Opening transaction...");
			em.getTransaction().begin();
			log.trace("Persisting entity...");
			em.persist(magazine);
			log.trace("Commiting transaction to database...");
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Creating magazine failed!");
			throw new DAOException("Creating magazine failed!", e);
		}

		log.trace("Returning Magazine...");
		log.info(magazine + " is added to database!");
		return magazine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Magazine> readAll() throws DAOException {
		log.info("Getting all magazines from database...");
		
		List<Magazine> magazineList = new ArrayList<>();

		try {
			log.trace("Finding entities...");
			Query query = (Query) em.createQuery("SELECT e FROM Magazine e");
			magazineList = ((javax.persistence.Query) query).getResultList();
		} catch (Exception e) {
			log.error("Getting list of magazines failed!");
			throw new DAOException("Getting list of magazines failed!", e);
		}

		log.trace("Returning list of magazines...");
		return magazineList;
	}

	@Override
	public Magazine readByID(int id) throws DAOException {
		log.info("Getting magazine by id from database...");

		Magazine magazine = null;

		try {
			log.trace("Finding entity...");
			magazine = em.find(Magazine.class, id);
		} catch (Exception e) {
			log.error("Getting magazine by id failed!");
			throw new DAOException("Getting magazine by id failed!", e);
		}

		log.trace("Returning Magazine...");
		log.info(magazine + " is getted from database!");
		return magazine;
	}

	@Override
	public boolean updateByID(Magazine magazine) throws DAOException {
		log.info("Updating magazine by id in database...");
		
		boolean result = false;

		try {
			log.trace("Opening transaction...");
			em.getTransaction().begin();
			log.trace("Updating entity...");
			em.merge(magazine);
			log.trace("Commiting transaction to database...");
			em.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			log.error("Updating magazine failed!");
			throw new DAOException("Updating magazine failed!", e);
		}

		if (result == false) {
			log.info("Updating magazine failed!");
		} else {
			log.trace("Returning result...");
			log.info("Magazine with ID#" + magazine.getId() + " is updated in database!");
		}
		return result;
	}

	@Override
	public boolean delete(int id) throws DAOException {
		log.info("Deleting magazine by id from database...");

		boolean result = false;

		try {
			log.info("Getting user by id from database...");
			Magazine magazine = readByID(id);
			log.trace("Opening transaction...");
			em.getTransaction().begin();
			log.trace("Removing entity...");
			em.remove(magazine);
			log.trace("Commiting transaction to database...");
			em.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			log.error("Deleting magazine failed!");
			throw new DAOException("Deleting magazine failed!", e);
		}

		if (result == false) {
			log.info("Deleting magazine failed!");
		} else {
			log.trace("Returning result...");
			log.info("Magazine with ID#" + id + " is deleted from database!");
		}
		return result;
	}
}