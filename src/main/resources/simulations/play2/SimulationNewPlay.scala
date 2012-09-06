package play2

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._

class SimulationNewPlay extends Simulation {

	def apply = {
		val httpConf = httpConfig
			.baseURL("http://localhost:9000")
			.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
			.hostHeader("localhost:9000")
			.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:11.0) Gecko/20100101 Firefox/11.0")

		val scn = scenario("Play2 Computer Database Sample")
			.loop(chain
				.exec(http("Home").get("/"))
				.pause(2)
				.exec(http("Search Apple")
					.get("/computers")
					.queryParam("f", "Apple")
					.check(
						md5.is("a59e79ab53eef2883d72b8f8398c9ac3"),
						regex("13 computers found")))
				.exec(http("Bootstrap CSS")
					.get("/assets/stylesheets/bootstrap.min.css"))
				.pause(2)
				.exec(http("Go to next page")
					.get("/computers")
					.queryParam("p", "1")
					.queryParam("f", "Apple"))
				.pause(6)
				.exec(http("Select Apple Network Server")
					.get("/computers/403"))
				.pause(6)
				.exec(http("Cancel")
					.get("/computers"))).times(1)

		List(scn.configure.users(1).protocolConfig(httpConf))
	}
}
