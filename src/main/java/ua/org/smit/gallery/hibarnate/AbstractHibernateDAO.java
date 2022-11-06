package ua.org.smit.gallery.hibarnate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public abstract class AbstractHibernateDAO<T> {

    Class<T> clazz;
    Transaction currentTransaction;
    Session currentSession;

    public final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T findOne(int id) {
        this.openCurrentSession();
        T entity = currentSession.get(clazz, id);
        this.closeCurrentSession();
        return entity;
    }

    public List<T> findAll() {
        this.openCurrentSession();
        List<T> items = currentSession.createQuery("from " + clazz.getName() + " E order by E.id desc").list();
        this.closeCurrentSession();
        return items;
    }

    public List<T> getByIds(List<Integer> ids) {
        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E where E.id in (:list) order by E.id desc";
        Query query = currentSession.createQuery(hql);
        query.setParameterList("list", ids);
        query.setMaxResults(ids.size());
        List<T> items = query.getResultList();
        this.closeCurrentSession();
        return items;
    }

    public T create(final T entity) {
        this.openCurrentSessionwithTransaction();
        currentSession.persist(entity);
        this.closeCurrentSessionwithTransaction();
        return entity;
    }

    public T update(final T entity) {
        this.openCurrentSessionwithTransaction();
        T item = (T) currentSession.merge(entity);
        this.closeCurrentSessionwithTransaction();
        return item;
    }

    public void delete(final T entity) {
        this.openCurrentSessionwithTransaction();
        currentSession.delete(entity);
        this.closeCurrentSessionwithTransaction();
    }

    public void deleteById(int entityId) {
        this.openCurrentSessionwithTransaction();
        final T entity = findOne(entityId);
        delete(entity);
        this.closeCurrentSessionwithTransaction();
    }

    public List<Integer> getAllIds() {
        this.openCurrentSession();
        String hql = "select " + clazz.getName() + ".id from " + clazz.getName();
        List<Integer> items = currentSession.createQuery(hql).list();
        this.closeCurrentSession();
        return items;
    }

    public List<Integer> getLastIds(int limit) {
        this.openCurrentSession();
        String hql = "select E.id from " + clazz.getName() + " E order by E.id desc";
        List<Integer> items = currentSession.createQuery(hql).setMaxResults(limit).list();
        this.closeCurrentSession();
        return items;
    }

    void openCurrentSession() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
    }

    void openCurrentSessionwithTransaction() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
    }

    void closeCurrentSession() {
        currentSession.close();
    }

    void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

}
