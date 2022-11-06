package ua.org.smit.gallery.hibarnate;

import java.util.List;
import java.util.Optional;
import org.hibernate.query.Query;
import ua.org.smit.gallery.album.AlbumInfo;

public class AlbumInfoDAO extends AbstractHibernateDAO< AlbumInfo> {

    public AlbumInfoDAO(Class<AlbumInfo> aClass) {
        setClazz(aClass);
    }

    public Optional<AlbumInfo> findByAlias(String alias) {
        Optional<AlbumInfo> optional = Optional.empty();

        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E where E.alias = :alias";
        Query query = currentSession.createQuery(hql);
        query.setParameter("alias", alias);
        query.setMaxResults(1);
        if (!query.getResultList().isEmpty()) {
            AlbumInfo entity = (AlbumInfo) query.getSingleResult();
            optional = Optional.of(entity);
        }
        this.closeCurrentSession();

        return optional;
    }

    public List<String> getAllAliases() {
        this.openCurrentSession();
        String hql = "select E.alias from " + clazz.getName()
                + " E order by E.id desc";
        List<String> items = currentSession.createQuery(hql).list();
        this.closeCurrentSession();
        return items;
    }

}
