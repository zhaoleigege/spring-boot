package pivotal

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PerformanceSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/plain")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling")

  val scn = scenario("PerformanceSimulation")
    .repeat(10) {
      exec(http("GET /io").get("/io"))
    }

  setUp(
    scn.inject(atOnceUsers(1000))
  ).protocols(httpProtocol)
}