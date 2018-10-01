package org.evil.rmv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.apache.commons.io.input.BOMInputStream
import org.springframework.stereotype.Component

@Component
class RmvStationDataMapping{

    fun readStations(): List<Stop> {

        val format = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withQuote('"')
                .withDelimiter(';')
        val reader =
                BOMInputStream(RmvApplication::class.java.getResource("/csv/RMV_Haltestellen.csv")
                        .openStream()).reader()
       return CSVParser.parse(reader, format).records.map {
            Stop(
                    hfasId = it["HAFAS_ID"].toLong(),
                    rmvId = it["RMV_ID"],
                    dhId = it["DHID"],
                    stopName = it["HST_NAME"],
                    planName = it["NAME_FAHRPLAN"],

                    iplX = it["X_IPL_WERT"],
                    iplY = it["Y_IPL_WERT"],
                    wgs84X = it["X_WGS84"],
                    wgs84Y = it["Y_WGS84"],
                    lno = it["LNO"],
                    isTrainStation = it booleanFrom "IST_BAHNHOF",
                    validFrom = it["GUELTIG_AB"],
                    validThru = it["GUELTIG_BIS"],
                    isRmvStation = it booleanFrom "VERBUND_1_ISTGLEICH_RMV",
                    state = it["LAND"],
                    rp = it["RP"],
                    stateDistrict = it["LANDKREIS"],
                    municipality = it["GEMEINDENAME"],
                    cityDistrict = it["ORTSTEILNAME"],
                    agsLand = it["AGS_LAND"],
                    agsRp = it["AGS_RP"],
                    agsLk = it["AGS_LK"],
                    agsG = it["AGS_G"],
                    agsOt = it["AGS_OT"])
        }
    }
}

private infix fun CSVRecord.booleanFrom(fieldName: String) = when (this[fieldName]) {
    "1" -> true
    else -> false
}