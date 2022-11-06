package ua.org.smit.gallery.hibarnate;

import java.util.List;
import java.util.Optional;
import org.hibernate.query.Query;
import ua.org.smit.gallery.user.photomodel.PhotomodelInfo;

public class PhotomodelsDAO extends AbstractHibernateDAO< PhotomodelInfo> {

    public PhotomodelsDAO(Class<PhotomodelInfo> aClass) {
        setClazz(aClass);
    }

    public Optional<PhotomodelInfo> findByAlias(String alias) {
        Optional<PhotomodelInfo> result = Optional.empty();

        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E where E.alias = :alias";
        Query query = currentSession.createQuery(hql);
        query.setParameter("alias", alias);
        query.setMaxResults(1);

        if (!query.getResultList().isEmpty()) {
            PhotomodelInfo entity = (PhotomodelInfo) query.getSingleResult();
            result = Optional.ofNullable(entity);
        }

        this.closeCurrentSession();

        return result;
    }

    public List<PhotomodelInfo> findByAlbumId(int albumId) {
        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E JOIN E.albums A WHERE A = :albums";
        Query query = currentSession.createQuery(hql);
        query.setParameter("albums", albumId);
        List<PhotomodelInfo> items = query.getResultList();
        this.closeCurrentSession();
        return items;
    }

    public List<PhotomodelInfo> getAllWithAlbumsIds() {
        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E ";
        Query query = currentSession.createQuery(hql);
        List<PhotomodelInfo> items = query.getResultList();
        this.closeCurrentSession();
        return items;
    }
}
