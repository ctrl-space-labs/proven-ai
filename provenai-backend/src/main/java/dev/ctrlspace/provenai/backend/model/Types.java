package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Types {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "type_category")
    private String typeCategory;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCategory() {
        return typeCategory;
    }

    public void setTypeCategory(String typeCategory) {
        this.typeCategory = typeCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Types types = (Types) o;
        return Objects.equals(id, types.id) && Objects.equals(typeCategory, types.typeCategory) && Objects.equals(name, types.name) && Objects.equals(description, types.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeCategory, name, description);
    }
}
