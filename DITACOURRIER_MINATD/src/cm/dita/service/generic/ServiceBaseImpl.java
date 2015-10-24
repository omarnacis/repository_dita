/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.dita.service.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;




import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.exception.ApplicationException;
import cm.dita.object.model.OMBase;
import cm.dita.beans.QueryParameter;
import cm.dita.beans.ResultDataQuery;

/**
 *
 * @author bertrand
 */
//@Transactional
public abstract class ServiceBaseImpl<T extends OMBase> implements IServiceBase<T> {
    
    /**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	@Override
    public abstract IDaoBase<T> getDao();
 
    @Transactional
    public void delete(Integer identifier) throws ApplicationException {
    
    	try {	    	
	        getDao().delete(identifier);	       
	       
    	} catch (Exception e) {
			
			throw new ApplicationException(" erreur de suppression de l'entit�["+getDao().getOMClass().getSimpleName()+ "] en base", e, 1);
		}
    }
 
    @Transactional  
    public void delete(T entity) throws ApplicationException{
    
    	try {
    		getDao().delete(entity);
    		
		} catch (Exception e) {
			
			throw new ApplicationException("impossible de supprimer l'entit� ["+ getDao().getOMClass()+"]", e, 14);
		}    	        
    }
 
    @Override
    public T get(Class<T> entityClass, Serializable id) throws ApplicationException{
    	getDao().getCurrentSession().clear();//new
        return getDao().get(entityClass, id);
    }
 
    @Override
    public List<T> list() throws ApplicationException{
    	getDao().getCurrentSession().clear();//new
        return getDao().list();
    }
 
    @Override
    public T load(Class<T> entityClass, Serializable identifiant) throws ApplicationException{
    	T om = null;
    	try {			
    		//getDao().getCurrentSession().clear();//new
    		om = getDao().load(entityClass, identifiant);
    		//getDao().getCurrentSession().close();  
    	} catch (Exception e) {
			return om;
		}
    	return om;
    }
 
    @Transactional
    public T load(Serializable id) throws ApplicationException{
    	T om = null;
    	try {
    		//getDao().getCurrentSession().flush();
    		 om = getDao().load(id);
    		// getDao().getCurrentSession().close();    		
		} catch (Exception e) {
			e.printStackTrace();
		}
        return om;
    }
 
    @Transactional
    public T save(T entity) throws ApplicationException{
    	try {   		
    		 getDao().save(entity);    		
    	} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("impossible de persiter l'entité ["+getDao().getOMClass().getSimpleName()+ "] en base", e, 2);
			
		}
    	return entity;
    }
    
    
    
    
    @Override
    public T load(Serializable id,String[] getters) throws ApplicationException{
    	T om = null;
    	try {    		
    		
		   	om = getDao().load(id,getters);
		   
    	} catch (Exception e) {
			return om;
		}
    	return om;
    }
    
    @Transactional
    public T update(T entity) throws ApplicationException{
    	
    	try { 
	    	getDao().update(entity);
	    	
    	} catch (Exception e) {
    		
    	}
    	return entity;
    }
    
    @Override
    public List<T> list(String[] getters) throws ApplicationException{
    	try {
    		
			return getDao().list(getters);
		} catch (Exception e) {
			// logger l'erreur
			throw new ApplicationException("erreur de recuperation de la liste de ["+getDao().getOMClass().getName()+"]  avec ses dependances", e, 15);
		}      
    }
    
    @Override
    public T executeNameQuery(String queryName , Map<String,Object> parameters) throws ApplicationException{
    	T om = null;
    	try {
    		
			om = getDao().executeNameQuery(queryName, parameters);
			//getDao().getCurrentSession().close();
		} catch (Exception e) {
			return om;
		}
    	return om;
    }
    
    @Override
    public List<T> executeNameQueryAndGetListResult(String queryName , Map<String,Object> parameters) throws ApplicationException{
    
    	List<T> list = new ArrayList<T>();
    	try {
    		 		
    		list = getDao().executeNameQueryAndGetListResult(queryName, parameters);			
		} catch (Exception e) {
			//faire un loggue ici
			return list;
		}
    	return list;    	
    }
    
    
   @Override
   public List<ResultDataQuery> executeListNameQuery(
			List<QueryParameter> queryParameters) throws ApplicationException{
    	return getDao().executeListNameQuery(queryParameters);    	
    }
   
   
   
	@Transactional
	public void deleteVersusDesabled(T entity,
			String nameFieldUsedToDesabledRecord) throws ApplicationException {
		
    	try {			
   
    		getDao().deleteVersusDesabled(entity, nameFieldUsedToDesabledRecord);
    		
	   	} catch (Exception e) {
			
				throw new ApplicationException("impossible de supprimer l'entité ["+getDao().getOMClass().getSimpleName()+ "] en base", e, 2);
			
		}
	}

	@Override
	public List<T> listVersusEnabled(String nameFieldUsedToDesabledRecord, String[] getters)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return getDao().listVersusEnabled(nameFieldUsedToDesabledRecord, getters);
	}

	@Override
	public List<T> listVersusDesabled(String nameFieldUsedToDesabledRecord ,String[] getters)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return getDao().listVersusDesabled(nameFieldUsedToDesabledRecord,getters);
	}
   
	public List<T> listVersusEnabledNewWithLazy(Map<String, Object> mapParameter,String[] getters) throws ApplicationException {
		
		return getDao().listVersusEnabledNewWithLazy(mapParameter,getters);
	}
	
	@Transactional
    public	void deleteAll() throws ApplicationException{
    	
    	try {
    		
			getDao().deleteAll();		
			
		} catch (Exception e) {
			
			throw new ApplicationException("impossible de vider la table"+getDao().getOMClass().getSimpleName(),e,15);
		}
	}
	
	/*  @Override
	    public void saveAll(List<T> liste) throws ApplicationException{
		  Transaction transaction = getDao().getCurrentSession().getTransaction();
	    	try {
	    		transaction.begin();
				getDao().saveAll(liste);		
				transaction.commit();
				
			} catch (Exception e) {
				transaction.rollback();
				throw new ApplicationException("impossible de vider la table"+getDao().getOMClass().getSimpleName(),e,15);
			}
	    	
	    }
	*/
	  
	  @Transactional
		public void saveAll(List<T> listEntity)throws ApplicationException{
				
			try {
				for(T om : listEntity){	
					
					getDao().save(om);
				}
			
			} catch (Exception e) {
			
				throw new ApplicationException("Probl�me lors du persist de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
			}
		}
	  
	  @Override
	  public List<T> findByCritaries(Map<String, Object> mapParameter,String op,boolean deleted){
		  
		  return getDao().findByCritaries(mapParameter,op, deleted);
	  }
	  
	  
	/*  @Override
	  public List<T> findByCritariesOP(Map<String, Object> mapParameter,String op,boolean deleted){
		  
		  return getDao().findByCritariesOP(mapParameter,op, deleted);
	  }*/
	  
	  public List<T> findByCritariesWithLazy(Map<String, Object> mapParameter,boolean deleted,String[] getters){
		  
		  return getDao().findByCritariesWithLazy(mapParameter, deleted, getters);
	  }
	  /*public Object backup(){
		  
		  Query backupQuery = getDao().getCurrentSession().createQuery("objectdb backup");
		    backupQuery.setParameter("target", new java.io.File("c:\\backup"));
		  return  backupQuery.getSingleResult();
		  
	  }*/
	  
	 
	
	 
}
