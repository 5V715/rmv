package org.evil.rmv

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RmvApplication

fun main(args: Array<String>) {
    val context = runApplication<RmvApplication>(*args)
    context.getBean(StopRepository::class.java)
            .saveAll(context.getBean(RmvStationDataMapping::class.java).readStations()
            )

}