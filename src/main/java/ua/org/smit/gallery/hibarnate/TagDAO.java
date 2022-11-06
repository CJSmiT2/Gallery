package ua.org.smit.gallery.hibarnate;

import java.util.Optional;
import org.hibernate.query.Query;
import ua.org.smit.gallery.tag.Tag;

public class TagDAO extends AbstractHibernateDAO<Tag> {

    public TagDAO(Class<Tag> aClass) {
        setClazz(aClass);
    }

    public Optional<Tag> findByName(String name) {
        Optional<Tag> result = Optional.empty();

        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E where E.name = :name";
        Query query = currentSession.createQuery(hql);
        query.setParameter("name", name);
        query.setMaxResults(1);

        if (!query.getResultList().isEmpty()) {
            Tag entity = (Tag) query.getSingleResult();
            result = Optional.ofNullable(entity);
        }

        this.closeCurrentSession();

        return result;
    }
}
