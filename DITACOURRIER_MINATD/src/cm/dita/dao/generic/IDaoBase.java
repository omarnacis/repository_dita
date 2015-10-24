package cm.dita.dao.generic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import cm.dita.beans.QueryParameter;
import cm.dita.beans.ResultDataQuery;
import cm.dita.exception.ApplicationException;
import cm.dita.object.model.IOM;
public interface IDaoBase<T extends IOM> extends IOM{//extends Repository<T,Serializable>,IOM {
	
	
    /**
     * . Retourne la classe de l objet metier.
     *
     * @return la classe de l'objet metier
     */
    Class<T> getOMClass();
 
    /**
     *
     * @param entityClass
     *            entityClass.
     * @param identifiant
     *            identifiant.
     * @return Class<T>.
     */
    T load(Class<T> entityClass, Serializable identifiant) throws ApplicationException;
 
    /**
     *
     * @param entityClass
     *            entityClass.
     * @param id
     *            identifiant.
     * @return Class<T>.
     */
    T get(Class<T> entityClass, Serializable id) throws ApplicationException;
 
    /**
     * loads an object from its id. Returns null if no object was found
     *
     * @param id
     *            identifier
     *
     * @return Class<T>.
     */
    T load(Serializable id) throws ApplicationException;
 
    /**
     * @return List<T>.
     */
    List<T> list();
 
    /**
     * @param entity
     *            objet.
     */
    void delete(T entity) throws ApplicationException;
 
    /**
     * Deletion by identifier
     *
     * @param identifier
     * the identifier of the object to delete
     */
    void delete(Serializable identifier) throws ApplicationException;
 
    /**
     * @param entity
     *            objet.
     * @return the object
     */
    T save(final T entity) throws ApplicationException;
    
    /**
     * 
     * @param entity
     * @return
     */
    T update(T entity) throws ApplicationException;
    
    
  
    
    /**
     * 
     * @param id
     * @param getters
     * @return
     */
    T load(Serializable id,String[] getters) throws ApplicationException;
    
    /**
     * 
     * @param getters
     * @return
     */
    List<T> list(String[] getters) throws ApplicationException;
    /**
     * 
     * @param queryName
     * @param mapParameter
     * @return
     * @throws ApplicationException
     */
    T executeNameQuery(String queryName,Map<String, Object> mapParameter) throws ApplicationException;
    
    /**
     * 
     * @param queryName
     * @param mapParameter
     * @return
     * @throws ApplicationException
     */
    List<T> executeNameQueryAndGetListResult(String queryName, Map<String, Object> mapParameter)
			throws ApplicationException;
    
    
    /**
     * execute un liste de requetes nomm�es 
     * @param queryParameters
     * @return
     * @throws ApplicationException
     */
    List<ResultDataQuery> executeListNameQuery(List<QueryParameter> queryParameters) throws ApplicationException;
    
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
    
    /**
     * supprimer tous les tuplets d'une table
     * @throws ApplicationException
     */
    void deleteAll() throws ApplicationException;

	int executeNameQueryUpdate(String queryName,
			Map<String, Object> mapParameter) throws ApplicationException;

	void saveAll(List<T> liste) throws ApplicationException;

	List<T> findByCritaries(Map<String, Object> mapParameter,String op,boolean deleted)
			throws ApplicationException;

	public List<T> findByCritariesWithLazy(Map<String, Object> mapParameter,boolean deleted,String[] getters) throws ApplicationException;
	
	public List<T> listVersusEnabledNewWithLazy(
			Map<String, Object> mapParameter, String[] getters)
			throws ApplicationException;
	
	
	List<Object[]> executeNameQueryAndGetListResultObject(String queryName,
			Map<String, String> mapParameter) throws ApplicationException;

	/**
     * retourne la session courante d'hibernte
     * @return
     */
	EntityManager getCurrentSession();

}
