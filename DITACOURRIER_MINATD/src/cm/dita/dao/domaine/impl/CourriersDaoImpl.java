package cm.dita.dao.domaine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cm.dita.constant.IConstance;
import cm.dita.dao.domaine.inter.ICourriersDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.Courriers;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;

public class CourriersDaoImpl extends DaoBaseImpl<Courriers> implements ICourriersDao {
	
	/*public List<Courriers> listVersusEnabled(String nameFieldUsedToDesabledRecord,
			String[] getters) throws ApplicationException {
		
		//Initialisation
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Courriers> criteriaQuery = criteriaBuilder.createQuery(getOMClass());
		Root<Courriers> from = criteriaQuery.from(getOMClass());
		
		//EXPRESSION	
		
		Expression<Long> path = from.get("dateUseToSortData");
		Expression<Long> param = criteriaBuilder.parameter(Long.class);
		Expression<Long> diff1 = criteriaBuilder.diff(param,path);
		
		//PREDICATE
		criteriaQuery.orderBy(criteriaBuilder.desc(from.get(IConstance.FIELD_SORT)));
		Predicate predicate = criteriaBuilder.equal(from.get(IConstance.FIELD_DELETE), false);
		//Predicate predicate2 = criteriaBuilder.le(from.get("pint"), arg2);
		criteriaQuery.where(predicate);	
		
		query.setParameter("arg1", arg1);
		
		//execution et retour
		CriteriaQuery<T> select = criteriaQuery.select(from);
		TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
		List<T> resultList = typedQuery.getResultList();
		return resultList;
	}
	*/
	
	
	@SuppressWarnings("unchecked")
	public List<Courriers> listCourrierLater(User userConnect,Long date_limit) {
		// TODO Auto-generated method
    	Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("limit_date", date_limit);
		Long date_current=System.currentTimeMillis();
		
		//Query query=dao.getCurrentSession().createQuery("select c from Courriers c where (c.dateUseToSortData-:date_current) > :limit_date");
		//query.setParameter("limit_date", date_limit);
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Courriers> criteriaQuery = criteriaBuilder.createQuery(Courriers.class);
		Root<Courriers> courrier = criteriaQuery.from(Courriers.class);
		
		//Expression avg = criteriaBuilder.avg(employee.<Number>get("salary"));
		
		
		Expression<Long> path = courrier.get("dateUseToSortData");
		Expression<Long> param = criteriaBuilder.parameter(Long.class,"date_current");
		Expression<Long> diff1 = criteriaBuilder.diff(param,path);
		//criteriaQuery.multiselect(diff1);
		
		
		criteriaQuery.where(criteriaBuilder.ge(diff1, date_limit));
		Query query = getEntityManager().createQuery(criteriaQuery);
		query.setParameter("date_current", date_current);
		//Employee result2 = (Employee)query.getSingleResult();
		
		return query.getResultList();
	}
	
public List<Courriers> listCourrierSearch(String chaine){
		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();		
		CriteriaQuery<Courriers> query = cb.createQuery(Courriers.class);

		Root<Courriers> personRoot = query.from(Courriers.class);

		//Where clause
		/*query.where(
		    cb.function(
		        "CONTAINS", Boolean.class, 
		         personRoot.<String>get("courmots"), 		        
		        cb.parameter(String.class, "containsCondition")));

		TypedQuery<Courriers> tq = getEntityManager().createQuery(query);
		tq.setParameter("containsCondition", "%"+chaine+"%");*/
		
		query.where(
			    //Like predicate
			    cb.like(
			        //assuming 'lastName' is the property on the Person Java object that is mapped to the last_name column on the Person table.
			        personRoot.<String>get("courmots"),
			        //Add a named parameter called likeCondition
			        cb.parameter(String.class, "likeCondition")));

			TypedQuery<Courriers> tq = getEntityManager().createQuery(query);
			tq.setParameter("likeCondition", "%"+chaine+"%");
			//List<Courriers> people = tq.getResultList();
		
		return  tq.getResultList();
		
	}
	

}
