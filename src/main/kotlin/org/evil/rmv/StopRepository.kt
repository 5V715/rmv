package org.evil.rmv

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

interface StopRepository : CrudRepository<Stop, Long> {

    @Query("SELECT DISTINCT hfasId,municipality,stopName,planName" +
            " FROM Stop s" +
            " WHERE UPPER(s.municipality) LIKE UPPER(concat('%',concat(?1, '%')))" +
            " OR UPPER(s.stopName) LIKE UPPER(concat('%',concat(?1, '%')))" +
            " OR UPPER(s.planName) LIKE UPPER(concat('%',concat(?1, '%')))" +
            " ORDER BY hfasId ASC")
    fun findByName(query: String): List<Array<Any>>

}

data class SearchResult(val hfasId: Long,
                        val municipality: String,
                        val stopName: String,
                        val planName: String
)


@Entity
data class Stop(@Id val hfasId: Long,
                @Column(nullable = false) val rmvId: String,
                @Column(nullable = false) val dhId: String,
                @Column(nullable = false) val stopName: String,
                @Column(nullable = false) val planName: String,
                @Column(nullable = false) val iplX: String,
                @Column(nullable = false) val iplY: String,
                @Column(nullable = false) val wgs84X: String,
                @Column(nullable = false) val wgs84Y: String,
                @Column(nullable = false) val lno: String,
                @Column(nullable = false) val isTrainStation: Boolean,
                @Column(nullable = false) val validFrom: String,
                @Column(nullable = false) val validThru: String,
                @Column(nullable = false) val isRmvStation: Boolean,
                @Column(nullable = false) val state: String,
                @Column(nullable = false) val rp: String,
                @Column(nullable = false) val stateDistrict: String,
                @Column(nullable = false) val municipality: String,
                @Column(nullable = false) val cityDistrict: String,
                @Column(nullable = false) val agsLand: String,
                @Column(nullable = false) val agsRp: String,
                @Column(nullable = false) val agsLk: String,
                @Column(nullable = false) val agsG: String,
                @Column(nullable = false) val agsOt: String
)