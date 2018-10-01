package org.evil.rmv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RmvStopService(val repo: StopRepository) {

    companion object {
        private val OUTPUT_FORMAT =
                CSVFormat.DEFAULT
                        .withDelimiter(',')
                        .withHeader("hfasId",
                                "rmvId",
                                "dhId",
                                "stopName",
                                "planName",
                                "iplX",
                                "iplY",
                                "wgs84X",
                                "wgs84Y",
                                "lno",
                                "isTrainStation",
                                "validFrom",
                                "validThru",
                                "isRmvStation",
                                "state",
                                "rp",
                                "stateDistrict",
                                "municipality",
                                "cityDistrict",
                                "agsLand",
                                "agsRp",
                                "agsLk",
                                "agsG",
                                "agsOt"
                        )
    }

    @GetMapping("/stop/{hfasId}", produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
    fun getById(@PathVariable("hfasId") hfasId: Long) =
            repo.findById(hfasId).run {
                ResponseEntity(this.orElseThrow { IllegalStateException("boom") }
                        , HttpStatus.OK)
            }

    @GetMapping("/search/{query}", produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
    fun stopSearch(@PathVariable query: String) =
            ResponseEntity(repo.findByName(query).map { SearchResult(it[0] as Long,
                    it[1] as String, it[2] as String, it[3] as String) }, HttpStatus.OK)

    @GetMapping("/csv", produces = ["application/csv"])
    fun toCsv(): ResponseEntity<String> {
        val result = StringBuilder()
        val printer = CSVPrinter(result, OUTPUT_FORMAT)
        repo.findAll().forEach {
            printer.printRecord(
                    it.hfasId,
                    it.rmvId,
                    it.dhId,
                    it.stopName,
                    it.planName,
                    it.iplX,
                    it.iplY,
                    it.wgs84X.replace(',', '.'),
                    it.wgs84Y.replace(',', '.'),
                    it.lno,
                    it.isTrainStation,
                    it.validFrom,
                    it.validThru,
                    it.isRmvStation,
                    it.state,
                    it.rp,
                    it.stateDistrict,
                    it.municipality,
                    it.cityDistrict,
                    it.agsLand,
                    it.agsRp,
                    it.agsLk,
                    it.agsG,
                    it.agsOt
            )
        }
        return ResponseEntity(result.toString(), HttpStatus.OK)
    }

}
