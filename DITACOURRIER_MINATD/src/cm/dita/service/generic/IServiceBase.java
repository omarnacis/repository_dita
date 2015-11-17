/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.dita.service.generic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;








import cm.dita.beans.QueryParameter;
import cm.dita.beans.ResultDataQuery;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.exception.ApplicationException;
import cm.dita.object.model.IOM;

/**
 *classe du service qui communique avec la dao
 * @author bertrand
 */
//@Transactional
public interface IServiceBase<T extends IOM> extends IOM {
    /**
     * reference de la dao 
     * @return 
     */
   IDaoBase<T> getDao();
     
    T load(Class<T> entityClass, Serializable identifiant) throws ApplicationException;
 
    T get(Class<T> entityClass, Serializable id) throws ApplicationException;
 
    T load(Serializable id) throws ApplicationException;
 
    List<T> list() throws ApplicationException;
 
    void delete(T entity) throws ApplicationException;
 
    void delete(Integer identifier) throws ApplicationException;
 
    T save(final T entity) throws ApplicationException;
    
    T update(T entity) throws ApplicationException;
    
    T load(Serializable id,String[] getters) throws ApplicationException;
    
    T executeNameQuery(String queryName , Map<String,Object> parameters) throws ApplicationException;
    
    List<T> executeNameQueryAndGetListResult(String queryName , Map<String,Object> parameters) throws ApplicationException;
    
    List<T> list(String[] getters) throws ApplicationException;
    
    List<ResultDataQuery> executeListNameQuery(
			List<QueryParameter> queryParameters) throws ApplicationException;
    
    /**
     *desactive unu tuple en base synonyme de suppression 
     * @param entity
     * @throws ApplicationException
     */
    void deleteVersusDesabled (T entity,String nameFieldUsedToDesabledRecord)throws ApplicationException;
    
    /**
     * liste les tuplets qui ne sont pas supprim�
     * @return
     * @throws ApplicationException
     */
    List<T> listVersusEnabled(String nameFieldUsedToDesabledRecord,String[] getters) throws ApplicationException;
    
    /**
     * nameFieldUsedToDesabledRecord  ceci est le nom du champ qui permet de specifier si un tuplet  est supprim� ou pas
     * liste les tuplets supprim�s 
     * @return
     * @throws ApplicationException
     */
    List<T> listVersusDesabled(String nameFieldUsedToDesabledRecord,String[] getters) throws ApplicationException;
   
    public List<T> listVersusEnabledNewWithLazy(Map<String, Object> mapParameter,String[] getters) throws ApplicationException ;

    void deleteAll() throws ApplicationException;
  
    
    void saveAll(List<T> listEntity)throws ApplicationException;

	
	public List<T> findByCritariesWithLazy(Map<String, Object> mapParameter,boolean deleted,String[] getters) throws ApplicationException;
  
	
	
	List<T> findByCritaries(Map<String, Object> mapParameter, String op,
			boolean deleted);
}
