package test.onlinecafe.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "configuration")
@NamedQueries({
        @NamedQuery(name = ConfigurationItem.DELETE, query = "DELETE FROM ConfigurationItem ci WHERE ci.id = :id"),
})
public class ConfigurationItem {
    public static final String DELETE = "ConfigurationItem.delete";

    @Id
    @NotNull
    @Size(min = 1, max = 20)
    private String id;

    @Column(name = "value")
    @NotNull
    @Size(max = 30)
    private String value;

    public ConfigurationItem() {
    }

    public ConfigurationItem(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        ConfigurationItem that = (ConfigurationItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigurationItem{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
