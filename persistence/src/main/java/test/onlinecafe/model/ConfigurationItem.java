package test.onlinecafe.model;

public class ConfigurationItem {
    private String id;
    private String value;

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