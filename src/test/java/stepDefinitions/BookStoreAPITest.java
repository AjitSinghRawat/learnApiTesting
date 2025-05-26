package stepDefinitions;

import apiEngine.Endpoints;
import apiEngine.IRestResponse;
import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthRequest;
import apiEngine.model.requests.ISBN;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.response.Book;
import apiEngine.model.response.BookListResponse;
import apiEngine.model.response.TokenResponse;
import apiEngine.model.response.UserAccount;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class BookStoreAPITest {
    // public static final String BASE_URL = "https://bookstore.toolsqa.com";
    public static final String USER_NAME = "Samridhi";
    public static final String PASSWORD = "Samridhi@4891";
    public static final String USER_ID = "88517c0a-40c8-4973-8b39-702968d51112";

    /*  public static final String USER_NAME_2 = "Digamber";
        public static final String PASWORD_2 = "Digamber@2891";
        public static final String USER_ID_2 = "c90b5148-c042-4dd4-b523-349ba25e835f";
        https://bookstore.toolsqa.com/swagger/#/
     */
    private static String isbnNumber;
    private static String jsonString;
    private static IRestResponse<UserAccount> userAccountResponse;
    private static Response response;
    private TokenResponse tokenResponse;
    private String token;

    @Given("I am an authorized user")
    public void i_am_an_authorized_user() {
        AuthRequest authRequest = new AuthRequest(USER_NAME, PASSWORD);
        tokenResponse = Endpoints.authenticateUser(authRequest).getBody();
        token = tokenResponse.token;
        System.out.println("Token is: -  " + token);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Given("A list of books are available")
    public void a_list_of_books_are_available() {
        IRestResponse<BookListResponse> bookResponse = Endpoints.getBooks();
        Assert.assertEquals(bookResponse.getStatusCode(), 200);
        List<Book> books = bookResponse.getBody().getBooks();
        Assert.assertTrue(books.size() > 0);
    }

    @When("User add a book to reading link")
    public void user_add_a_book_to_reading_link() {
        List<ISBN> isbn = new ArrayList<>();
        isbn.add(new ISBN(isbnNumber));
        AddBooksRequest addBookRequest = new AddBooksRequest(USER_ID, isbn);
        userAccountResponse = Endpoints.addBook(addBookRequest, token);
        //new RestResponse(AddBooksRequest.class, response);
    }

    @Then("The book is added")
    public void the_book_is_added() {
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @When("User remove a book from reading link")
    public void user_remove_a_book_from_reading_link() {
        RemoveBookRequest deleteBook = new RemoveBookRequest(USER_ID, isbnNumber);
        response = Endpoints.removeBook(deleteBook, tokenResponse.token);
    }

    @Then("The book is removed")
    public void the_book_is_removed() {
        Assert.assertEquals(response.getStatusCode(), 204);
    }

}
