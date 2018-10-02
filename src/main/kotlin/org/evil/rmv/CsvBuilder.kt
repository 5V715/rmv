package org.evil.rmv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object CsvResponseBuilder : (StopRepository) -> ResponseEntity<String> {

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

    override fun invoke(repo: StopRepository): ResponseEntity<String> {
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