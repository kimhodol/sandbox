package dev.hodol.sandbox.base

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import javax.persistence.*
import javax.persistence.CascadeType.PERSIST


@Entity
@Table(name = "authors")
open class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotNull
    @Size(max = 100)
    var name: String? = null,

    @OneToMany(mappedBy = "author", cascade = [PERSIST])
    var books: MutableList<Book> = mutableListOf()
) {
    constructor(name: String): this() {
        this.name = name;
    }

    override fun toString() = "Author(id=$id, name=$name, books=$books)"
}
