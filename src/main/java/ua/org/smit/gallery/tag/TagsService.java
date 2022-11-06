package ua.org.smit.gallery.tag;

import java.util.List;
import java.util.Optional;
import ua.org.smit.gallery.hibarnate.TagDAO;
import org.apache.log4j.Logger;

public class TagsService {

    static Logger log = Logger.getLogger(TagsService.class);

    private final TagDAO tagDAO = new TagDAO(Tag.class);

    public Tag createTag(String name) {
        log.info("Create tag by name '" + name + "'");
        Tag tag = new Tag();
        tag.setName(name);
        return tagDAO.create(tag);
    }

    public Tag getTag(int id) {
        return tagDAO.findOne(id);
    }

    public List<Tag> getAllTags() {
        return this.tagDAO.findAll();
    }

    public Optional<Tag> getTagByName(String name) {
        Optional<Tag> tag = this.tagDAO.findByName(name);
        if (!tag.isPresent()) {
            return Optional.empty();
        }
        tag.get().setAlbumsInfos();
        return tag;
    }

    public void removeFromTags(int imageAlias) {
        List<Tag> tags = this.tagDAO.findAll();
        for (Tag tag : tags) {
            if (tag.isHasImage(imageAlias)) {
                tag.removeImage(imageAlias);
                tagDAO.update(tag);
                log.info("Image by alias '" + imageAlias + "' has deleted "
                        + "from tag '" + tag.getName() + "'");
            }
        }
    }
}
