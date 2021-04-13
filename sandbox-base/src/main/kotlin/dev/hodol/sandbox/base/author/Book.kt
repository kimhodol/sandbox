package dev.hodol.sandbox.base.author

import javax.persistence.*

@Entity
@Table(name = "books")
open class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String? = null,

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: Author? = null,
) {
    constructor(title: String) : this() {
        this.title = title
    }

    override fun toString() = "Book(id=$id, title=$title, author=$author)"
}
