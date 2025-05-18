package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        tags = "",
        //features = "src/test/resources/functionalTests/E2E_Tests_BookStore.feature",
        //glue = {"stepDefinitions.BookStoreAPITest.java", ""},
        features = ("src/test/resources/functionalTests/"),
        glue = {"stepDefinitions"},
        plugin = {}
)

public class TestRunner extends AbstractTestNGCucumberTests {


}
