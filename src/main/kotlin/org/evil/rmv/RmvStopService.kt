package org.evil.rmv

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Controller
class RmvStopService(val repo: StopRepository) {

    @GetMapping("/stop/{hfasId}", produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
    fun getById(@PathVariable("hfasId") hfasId: Long) =
            repo.findById(hfasId).run {
                ResponseEntity(this.orElseThrow { IllegalStateException("boom") }
                        , HttpStatus.OK)
            }

    @GetMapping("/search/{query}", produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
    fun stopSearch(@PathVariable query: String) =
            ResponseEntity(repo.findByName(query).map {
                SearchResult(it[0] as Long,
                        it[1] as String, it[2] as String, it[3] as String)
            }, HttpStatus.OK)

    @GetMapping("/all")
    fun all() = ResponseEntity(repo.findAll(), HttpStatus.OK)

    @GetMapping("/csv", produces = ["application/csv"])
    fun toCsv(): ResponseEntity<String> = CsvResponseBuilder(repo)

    @GetMapping("/departures/{stationId}")
    fun departures(model: Model,
                   @PathVariable("stationId") stationId: Long,
                   @Value("\${API_KEY}") accessId: String,
                   restTemplate: RestTemplate): String {

        val data = stationData(stationId, accessId, restTemplate).body

        model.addAttribute("stationName", repo.findByHfasId(stationId).planName)
        model.addAttribute("stationId", stationId)
        model.addAttribute("initialData", data?.take(25))
        return "departures"
    }

    @GetMapping("/station-data/{stationId}", produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
    fun stationData(
            @PathVariable("stationId") stationId: Long,
            @Value("\${API_KEY}") accessId: String,
            restTemplate: RestTemplate): ResponseEntity<List<String>> {
        val response = restTemplate
                .exchange<DepartureResponse>("https://www.rmv.de/hapi/departureBoard?id={stationId}&accessId={accessId}&format=json",
                        HttpMethod.GET, uriVariables = *arrayOf(stationId, accessId))
        return ResponseEntity(
                when (response.body) {
                    is DepartureResponse -> {
                        response.body!!.departure.map {
                            "${(it.rtTime ?: it.time).subSequence(0, 5)} ${it.track} ${it.name} ${it.direction}"
                        }
                    }
                    else -> emptyList()
                }, HttpStatus.OK)
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
data class DepartureResponse(@JsonProperty("Departure") val departure: List<Departure> = emptyList())

@JsonIgnoreProperties(ignoreUnknown = true)
data class Departure(val name: String, val rtTime: String?, val time: String, val direction: String, val track: String?)
