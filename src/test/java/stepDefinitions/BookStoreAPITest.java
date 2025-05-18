package stepDefinitions;

import apiEngine.Endpoints;
import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthRequest;
import apiEngine.model.requests.ISBN;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.response.BookListResponse;
import apiEngine.model.response.TokenResponse;
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
    //private static String token;
    private static Response response;
    private TokenResponse tokenResponse;

    @Given("I am an authorized user")
    public void i_am_an_authorized_user() {
        AuthRequest authRequest = new AuthRequest(USER_NAME, PASSWORD);
        response = Endpoints.authenticateUser(authRequest);
        /*RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request
                .header("Content-Type", "application/json")
                .body(authRequest)
                .post("/Account/v1/GenerateToken");
        jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");*/
        tokenResponse = response.getBody().as(TokenResponse.class);
        System.out.println("Token is: -  " + tokenResponse.token);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Given("A list of books are available")
    public void a_list_of_books_are_available() {
        /*RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request
                .header("content-type", "application/json")
                .get("/bookstore/v1/books");*/
        response = Endpoints.getBooks();
        Assert.assertEquals(response.getStatusCode(), 200);
        BookListResponse listOfBooks = response.getBody().as(BookListResponse.class);


   /*     jsonString = response.asString();
        JsonPath jsonPath = new JsonPath(jsonString);*/

        // Get total number of books.
        // int getNumberOfBooks = jsonPath.getList("books").size();

        Assert.assertTrue(listOfBooks.getBooks().size() > 0);

        //Get isbn of the first book
        //isbnNumber = jsonPath.getString("books[0].isbn");
        isbnNumber = listOfBooks.getBooks().get(0).getIsbn();
        Assert.assertEquals(isbnNumber, "9781449325862");
        System.out.println("isbn number is: " + isbnNumber);

        //Get second Book title
        // String secondBookTitle = jsonPath.getString("books[1].title");
        Assert.assertEquals(listOfBooks.getBooks().get(1).getTitle(), "Learning JavaScript Design Patterns");

        //Get the author of all books:
        //List<String> listOfAuthors = jsonPath.getList("books.author");
        System.out.println("List of the author of all books:: " + listOfBooks.getBooks().get(2).getAuthor());

        //Get the title of the book with ISBN '9781449325862':
        //String bookTitle = jsonPath.getString("books.find{it.isbn == '9781449325862'}.title");
        for (int i = 0; i < listOfBooks.getBooks().size(); i++) {
            if (listOfBooks.getBooks().get(i).getIsbn().equalsIgnoreCase("9781449325862") == true) {
                String bookTitle = listOfBooks.getBooks().get(0).getTitle();
                System.out.println("Book Title: " + bookTitle);
            }
        }

    }

    @When("User add a book to reading link")
    public void user_add_a_book_to_reading_link() {
        List<ISBN> isbn = new ArrayList<>();
        isbn.add(new ISBN(isbnNumber));
        AddBooksRequest addBookRequest = new AddBooksRequest(USER_ID, isbn);
        /*RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request
                //.log().all()
                .header("Authorization", "Bearer " + tokenResponse.token)
                .header("Content-Type", "application/json")
                .body(addBookRequest)
                .post("/BookStore/v1/Books");*/
        response = Endpoints.addBook(addBookRequest, tokenResponse.token);
    }

    @Then("The book is added")
    public void the_book_is_added() {
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @When("User remove a book from reading link")
    public void user_remove_a_book_from_reading_link() {
        RemoveBookRequest deleteBook = new RemoveBookRequest(USER_ID, isbnNumber);
        /*RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request
                // .log().all()
                .header("Authorization", "Bearer " + tokenResponse.token)
                .header("Content-Type", "application/json")
                .body(deleteBook)
                .delete("/BookStore/v1/Book");*/
        response = Endpoints.removeBook(deleteBook, tokenResponse.token);
    }

    @Then("The book is removed")
    public void the_book_is_removed() {
        Assert.assertEquals(response.getStatusCode(), 204);
    }

}
