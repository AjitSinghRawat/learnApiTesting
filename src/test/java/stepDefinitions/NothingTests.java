package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class NothingTests {

    @Given("Everything is given")
    public void everything_is_given() {
        Assert.assertEquals(true, true);
    }

    @When("There is nothing")
    public void there_is_nothing() {
        Assert.assertEquals(true, true);
    }

    @Then("You have nothing")
    public void you_have_nothing() {
        Assert.assertEquals(true, true);
    }


}
