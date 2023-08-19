package mi.stat.model.entropy.tree;

import java.util.Objects;

public class Entity {
    String title;
    String value;

    public Entity(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(title, entity.title) && Objects.equals(value, entity.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, value);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}