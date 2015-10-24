package cm.dita.dao.generic;



import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cm.dita.beans.QueryParameter;
import cm.dita.beans.ResultDataQuery;
import cm.dita.constant.IConstance;
import cm.dita.exception.ApplicationException;
import cm.dita.object.model.OMBase;
import cm.dita.utils.MethodUtils;
import cm.dita.utils.ORMUtils;
import cm.dita.utils.ReflectionUtils;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Repository;

public class DaoBaseImpl <T extends  OMBase >  implements IDaoBase<T>{
	
	
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	
	  @Override
	    public Class<T> getOMClass() {
	        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
	        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	    }

	@Override
	public T load(Class<T> entityClass, Serializable identifiant)
			throws ApplicationException {
	    try{
		T result = getEntityManager().find(entityClass, identifiant);
		
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
        return  null;
	}

	@Override
    public T get(Class<T> entityClass, Serializable id) throws ApplicationException{
        final Object result = getEntityManager().find(entityClass, id);
        return (T) result;
    }

	@Override
	public T load(Serializable id) throws ApplicationException {
		// TODO Auto-generated method stub		
		final Object result = getEntityManager().find(getOMClass(),id);
		getEntityManager().merge(result);
        return (T) result;
	}

	@Override
	public List<T> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(T entity) throws ApplicationException {
		// TODO Auto-generated method stub
		getEntityManager().remove(entity);
		
	}

	@Override
	public void delete(Serializable identifier) throws ApplicationException {
		// TODO Auto-generated method stub
		getEntityManager().remove(identifier);
		
	}

	@Override
	public T save(T entity) throws ApplicationException {
		// TODO Auto-generated method stub		
		if (entity == null) {
			getEntityManager().persist(entity);
			return entity;
		} else {
			return getEntityManager().merge(entity);
		}
		
	}

	@Override
	public T update(T entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return getEntityManager().merge(entity);
	}

	@Override
	public T load(Serializable id, String[] getters)
			throws ApplicationException {
		
		T om = (T) getEntityManager().find(getOMClass(), id);
		if(getters != null && getters.length > 0){
			try {		
					ORMUtils.initLazyGetters(om, getters);			
			} catch (Exception e) {			
				throw new ApplicationException("erreur d'initialisation de l'entity  de nom ["+getOMClass().getSimpleName()+"]  message :"+e.getMessage(), 5);
			}
		}
		return om;
	}

	@Override
	public List<T> list(String[] getters) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T executeNameQuery(String queryName, Map<String, Object> mapParameter)
			throws ApplicationException {
		// TODO Auto-generated method stub
	
		Query query =getEntityManager().createNamedQuery(queryName);
		Iterator<String> it = mapParameter.keySet().iterator();
		 while (it.hasNext()) {
			String parameterName = (String) it.next();
			String parameterValue = (String) mapParameter.get(parameterName); 
			query.setParameter(parameterName, parameterValue);				
		}
		
		List<T> list = query.getResultList(); 
		
		 if(!list.isEmpty()) return (T)list.get(0);
		
		 return null;
		
	}

	@Override
	public List<T> executeNameQueryAndGetListResult(String queryName,
			Map<String, Object> mapParameter) throws ApplicationException {
		
		try {
			 Query query =	getEntityManager().createNamedQuery(queryName);
			 //query.
			 Iterator<String> it = mapParameter.keySet().iterator();
			 while (it.hasNext()) {
				String parameterName = (String) it.next();
				Object parameterValue = mapParameter.get(parameterName); 
				query.setParameter(parameterName, parameterValue);				
			}
			 
			 List list = query.getResultList();	
			
			 if(!list.isEmpty()) {
				 //Collections.sort(list);
				 return (List<T>)list;
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		throw new ApplicationException("problème lors de l'execution d'une requette nommée  concernant l'entité ["+getOMClass().getSimpleName()+"]", e, 7);
		}		
		return new ArrayList<T>();
	
	}
   /**
    * Execute une liste de requette nommé
    * @param queryParameters
    * @return
    * @throws ApplicationException
    */
	@Override
	public List<ResultDataQuery> executeListNameQuery(
			List<QueryParameter> queryParameters) throws ApplicationException {
		
		List<ResultDataQuery> resultDataQueries = new ArrayList<ResultDataQuery>();
		for(QueryParameter queryParameter : queryParameters){
			ResultDataQuery dataQuery = new ResultDataQuery();
			try {
				
				List<T> list = executeNameQueryAndGetListResult(queryParameter.getNameQuery(), queryParameter.getMapParameters());
				
				if(!list.isEmpty()){
					dataQuery.setCanonicalNameOfEntity(list.get(0).getClass().getCanonicalName());
				}else{
					dataQuery.setCanonicalNameOfEntity(null);
				}
				
				dataQuery.setListResult((List<Object>)(Object)list);
			} catch (Exception e) {				
				dataQuery.setCanonicalNameOfEntity(null);
				dataQuery.setListResult(new ArrayList<Object>());
			}			
			resultDataQueries.add(dataQuery);
		}		
		return resultDataQueries;
	}

	@Override
	public void deleteVersusDesabled(T entity,
			String nameFieldUsedToDesabledRecord) throws ApplicationException {
		// TODO Auto-generated method stub
		Field field = ReflectionUtils.getField(entity.getClass(), nameFieldUsedToDesabledRecord);
		if(field != null){
			MethodUtils.setValeurOfField(entity, field, true);
		}
		
		getEntityManager().merge(entity);
		
	}

	@Override
	public List<T> listVersusEnabled(String nameFieldUsedToDesabledRecord,
			String[] getters) throws ApplicationException {
		
		//Initialisation
		CriteriaBuilder criteriaBuilder =  getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getOMClass());
		Root<T> from = criteriaQuery.from(getOMClass());
		//PREDICATE
		criteriaQuery.orderBy(criteriaBuilder.desc(from.get(IConstance.FIELD_SORT)));
		Predicate predicate = criteriaBuilder.equal(from.get(IConstance.FIELD_DELETE), false);
		//Predicate predicate2 = criteriaBuilder.le(from.get("pint"), arg2);
		criteriaQuery.where(predicate);		
		
		//execution et retour
		CriteriaQuery<T> select = criteriaQuery.select(from);
		select.orderBy(criteriaBuilder.desc(from.get("dateUseToSortData")));
		TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
		List<T> resultList = typedQuery.getResultList();
		return resultList;
	}
	/**
	 * @see Liste des objets supprimer
	 * @param le champ ou sera effectué le test de suppression
	 * @param un tableau pour effectuer la profondeur de recuperation 
	 * @return la liste d'objets pretendu supprimer
	 * @author mbe
	 * 
	 */
	@Override
	public List<T> listVersusDesabled(String nameFieldUsedToDesabledRecord,
			String[] getters) throws ApplicationException {
		//Initialisation
				CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
				CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getOMClass());
				Root<T> from = criteriaQuery.from(getOMClass());
		//PREDICATE
				Predicate predicate = criteriaBuilder.equal(from.get(IConstance.FIELD_DELETE), true);
				//Predicate predicate2 = criteriaBuilder.le(from.get("pint"), arg2);
				criteriaQuery.where(predicate);		
				
		//execution et retour
				CriteriaQuery<T> select = criteriaQuery.select(from);
				TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
				List<T> resultList = typedQuery.getResultList();
				return resultList;
	}

	@Override
	public void deleteAll() throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see Execution d'une requete nommé pour la mise a jour
	 * @param queryName
	 * @param mapParameter
	 * @return
	 * @throws ApplicationException
	 */
	public int executeNameQueryUpdate(String queryName,
			Map<String, Object> mapParameter) throws ApplicationException {
		// TODO Auto-generated method stub
		
		try {
			 Query query =	getEntityManager().createNamedQuery(queryName);
			 //query.
			 Iterator<String> it = mapParameter.keySet().iterator();
			 while (it.hasNext()) {
				String parameterName = (String) it.next();
				Object parameterValue =  mapParameter.get(parameterName); 
				query.setParameter(parameterName, parameterValue);				
			}
			 
			 int r= query.executeUpdate();
			// getEntityManager().flush();
			 return r;
			
		} catch (Exception e) {
		throw new ApplicationException("problème lors de l'execution d'une requette nommée  concernant l'entité ["+getOMClass().getSimpleName()+"]", e, 7);
		}
		
	}
	/**
	 * @see Sauvegarde une liste d'objet
	 * @param liste d'objets
	 * @return vide
	 * @author mbe
	 * 
	 */
	@Override
	public void saveAll(List<T> liste) throws ApplicationException {	// TODO Auto-generated method stub
		
		
		for(T entity: liste){
	    	try {	
	    		
	    		getEntityManager().persist(entity);   
	    		
	    	} catch (Exception e) {
	    		
			}
    	}
		
	}
    /**
     * @see Fonction pour la rechercher par critere
     * @param la liste des parametres et leur valeur
     * @param le type d'operation AND ou OR
     * @return la liste d'objets
     * @author mbe 
     * 
     */
	@Override
	public List<T> findByCritaries(Map<String, Object> mapParameter,String op,
			boolean deleted) throws ApplicationException {
		
		Predicate predicate =null;
		
		
		//Initialisation
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getOMClass());
		Root<T> from = criteriaQuery.from(getOMClass());
		Predicate predicate2 = criteriaBuilder.equal(from.get(IConstance.FIELD_DELETE), deleted);
//PREDICATE
		
		Iterator<String> it = mapParameter.keySet().iterator();
		 while (it.hasNext()) {			 
			String parameterName = (String) it.next();
			Object parameterValue =  mapParameter.get(parameterName); 			
			criteriaBuilder.equal(from.get(parameterName), parameterValue);
					
		}
  //TEST 
		if(op.equalsIgnoreCase("and"))
		 predicate = criteriaBuilder.conjunction();	
		else
			if(op.equalsIgnoreCase("or"))
				predicate = criteriaBuilder.disjunction();
			
		criteriaQuery.where(criteriaBuilder.and(predicate,predicate2));		
		
//execution et retour
		CriteriaQuery<T> select = criteriaQuery.select(from);
		TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
		List<T> resultList = typedQuery.getResultList();
		return resultList;
	
	}

	@Override
	public List<T> findByCritariesWithLazy(Map<String, Object> mapParameter,
			boolean deleted, String[] getters) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public List<T> listVersusEnabledNewWithLazy(
			Map<String, Object> mapParameter, String[] getters)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<Object[]> executeNameQueryAndGetListResultObject(
			String queryName, Map<String, String> mapParameter)
			throws ApplicationException {
		
		try {
			 Query query =	getEntityManager().createNamedQuery(queryName);
			 //query.
			 Iterator<String> it = mapParameter.keySet().iterator();
			 while (it.hasNext()) {
				String parameterName = (String) it.next();
				String parameterValue = mapParameter.get(parameterName); 
				query.setParameter(parameterName, parameterValue);				
			}
			 
			 List list = query.getResultList();
			 //JOptionPane.showMessageDialog(null, list.size());
			 if(!list.isEmpty()) {
				 //Collections.sort(list);
				 return (List<Object[]>)list;
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		throw new ApplicationException("problème lors de l'execution d'une requette nommée  concernant l'entité ["+getOMClass().getSimpleName()+"]", e, 7);
		}		
		return null;
		
		
		//return null;
	}
	
	
// récupérer l'EntityManager courant
	public EntityManager getEntityManager() {
	if (em == null || !em.isOpen()) {
	//em = 
	//em.
	}
	return em;
	}
	
	// récupérer un EntityManager neuf
	public EntityManager getNewEntityManager() {
	if (em != null && em.isOpen()) {
	em.close();
	}
	//em = emf.createEntityManager();
	return em;
	}

	@Override
	public EntityManager  getCurrentSession() {
		// TODO Auto-generated method stub
		return em;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
		} 

	

}
