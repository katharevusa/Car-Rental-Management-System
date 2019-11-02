/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ejb.session.stateless;

import entity.CategoryEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author admin
 */
@Stateless
@Local(CategoryEntitySessionBeanLocal.class)
@Remote(CategoryEntitySessionBeanRemote.class)
public class CategoryEntitySessionBean implements CategoryEntitySessionBeanRemote, CategoryEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createCategory(CategoryEntity newCategory) {
        //set up the relatinoship with rental rate
        newCategory.setRentalRate(newCategory.getRentalRate());
        em.persist(newCategory);
        em.flush();
        return newCategory.getCategoryId();
    }
    
    @Override
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException
    {
          CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        
        if(category != null)
        {
            return category;
       }
        else
        {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }
    @Override
    public CategoryEntity retrieveCategoryByCategoryName(String categoryname) throws CategoryNotFoundException{
        Query query = em.createQuery("SELECT c FROM CategoryEntity c WHERE c.categoryname = :inCategoryname");
        query.setParameter("inCategoryname", categoryname);
        
        try
        {
            return (CategoryEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CategoryNotFoundException("Category " + categoryname + " does not exist!");
        }
    }
    
    @Override
    public void updateCategory(CategoryEntity category) {
        em.merge(category);
    }
    
@Override
    public void deleteCategory(Long categoryId) {
        //CategoryEntity category = retrieveCategoryByCategoryId(categoryId);
       // em.remove(category);
    }
    
    
}
