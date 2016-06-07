import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.Proxy

waiting {
	timeout = 2
}

environments {

	firefoxproxy {
    // String selenumHubUrl = System.getProperty("selenium.hub.url", "http://DEFAULT_GRID_URL")
    def capabilities = DesiredCapabilities.firefox()
    String PROXY = "localhost:8888"
    Proxy proxy = new Proxy()
    proxy.setHttpProxy(PROXY).setSslProxy(PROXY)
    capabilities.setCapability(CapabilityType.PROXY, proxy)

		driver = {
      def browser = new FirefoxDriver(capabilities)
      browser
		}
	}
}

baseUrl = "http://gebish.org"
reportsDir = 'build/tests'
