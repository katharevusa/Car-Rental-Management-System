/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;


public interface CategoryEntitySessionBeanRemote {
    public Long createCategory(CategoryEntity newCategory);
    
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    public CategoryEntity retrieveCategoryByCategoryName(String categoryname) throws CategoryNotFoundException;
    public void updateCategory(CategoryEntity category);
    public List<CategoryEntity> retrieveAllCategory();
    public void deleteCategory(Long categoryId);
}
