package com.cadman.app.banking.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cadman.app.banking.entity.AccountEntity;

@Repository
public class BankingRepositoryImpl implements BankingRepository {

	@PersistenceContext
	EntityManager em;

	@Transactional
	public void addAccount(AccountEntity acc) {
		em.persist(acc);
	}

	@Override
	public List<AccountEntity> getListOfAccounts() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
		Root<AccountEntity> root = cq.from(AccountEntity.class);
		cq.select(root);
		TypedQuery<AccountEntity> query = em.createQuery(cq);

		return query.getResultList();
	}

	@Override
	public AccountEntity getAccount(String accNumber) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
		Root<AccountEntity> root = cq.from(AccountEntity.class);
		cq.select(root).where(cb.equal(root.get("accNumber"), accNumber));
		TypedQuery<AccountEntity> query = em.createQuery(cq);

		return query.getSingleResult();
	}

	@Override
	public void updateBalance(double balance, String accNumber) throws Exception {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<AccountEntity> update = cb.createCriteriaUpdate(AccountEntity.class);
		Root<AccountEntity> root = update.from(AccountEntity.class);

		update.set("balance", balance);
		update.where(cb.equal(root.get("accNumber"), accNumber));

		Query query = em.createQuery(update);
		int result = query.executeUpdate();
		if (result == -1)
			throw new Exception("Update result not completed, check db connection.");

	}

	@Override
	public void transferFunds(double fromBalance, double toBalance, String fromAccountNo, String toAccountNo) {

		Query query = em.createQuery(

				"UPDATE AccountEntity t SET " + "t.balance = IF(t.accNumber = :fromAccountNo, 100.0, 100.0) "
						+ "WHERE t.accNumber IN(:fromAccountNo, :toAccountNo)");

		query.setParameter("fromAccountNo", fromAccountNo);
		query.setParameter("toAccountNo", toAccountNo);

		query.executeUpdate();
	}

}
