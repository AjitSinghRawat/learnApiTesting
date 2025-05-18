package apiTests;

import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthRequest;
import apiEngine.model.requests.ISBN;
import apiEngine.model.requests.RemoveBookRequest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class E2E_API_Tests {
    public static final String BASE_URL = "https://bookstore.toolsqa.com";
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
    private static String token;

    @Test(description = "Validate that a user is authorized.", priority = 0)
    public void verifyUserIsAuthorized() {
        AuthRequest authRequest = new AuthRequest(USER_NAME, PASSWORD);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("content-type", "application/json")
                .body(authRequest)
                .post("/Account/v1/Authorized");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1)
    public void generateToken() {
        AuthRequest authRequest = new AuthRequest(USER_NAME, PASSWORD);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("Content-Type", "application/json")
                .body(authRequest)
                .post("/Account/v1/GenerateToken");
        jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");
        System.out.println("Token is: -  " + token);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void getListOfBooks() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("content-type", "application/json")
                .get("/bookstore/v1/books");
        Assert.assertEquals(response.getStatusCode(), 200);
        jsonString = response.asString();
        JsonPath jsonPath = new JsonPath(jsonString);

        // Get total number of books.
        int getNumberOfBooks = jsonPath.getList("books").size();
        Assert.assertTrue(getNumberOfBooks > 0);

        //Get isbn of the first book
        isbnNumber = jsonPath.getString("books[0].isbn");
        Assert.assertEquals(isbnNumber, "9781449325862");
        System.out.println("isbn number is: " + isbnNumber);

        //Get second Book title
        String secondBookTitle = jsonPath.getString("books[1].title");
        Assert.assertEquals(secondBookTitle, "Learning JavaScript Design Patterns");

        //Get the author of all books:
        List<String> listOfAuthors = jsonPath.getList("books.author");
        System.out.println("List of the author of all books:: " + listOfAuthors);

        //Get the title of the book with ISBN '9781449325862':
        String bookTitle = jsonPath.getString("books.find{it.isbn == '9781449325862'}.title");
        System.out.println("Book Title: " + bookTitle);
    }

    @Test(priority = 3)
    public void listOfBooks() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/bookstore/v1/books");
        String jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        Assert.assertTrue(books.size() > 0);

        //Get isbn of the first book
        isbnNumber = JsonPath.from(jsonString).getString("books[0].isbn");
        Assert.assertEquals(isbnNumber, "9781449325862");

        //Get second Book title
        String secondBookTitle = JsonPath.from(jsonString).getString("books[1].title");
        Assert.assertEquals(secondBookTitle, "Learning JavaScript Design Patterns");

        //Get the author of all books:
        List<String> listOfAuthors = JsonPath.from(jsonString).getList("books.author");
        System.out.println("List of the author of all books:: " + listOfAuthors);

        //Get the title of the book with ISBN '9781449325862':
        String bookTitle = JsonPath.from(jsonString).getString("books.find{it.isbn == '9781449325862'}.title");
        System.out.println("Book Title: " + bookTitle);

    }

    @Test(priority = 4)
    public void addBookIList() {
        List<ISBN> isbn = new ArrayList<>();
        isbn.add(new ISBN(isbnNumber));
        AddBooksRequest addBookRequest = new AddBooksRequest(USER_ID, isbn);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .log().all()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(addBookRequest)
                .post("/BookStore/v1/Books");
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 5)
    public void deleteBookFromUserList() {
        RemoveBookRequest deleteBook = new RemoveBookRequest(USER_ID, isbnNumber);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .log().all()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(deleteBook)
                .delete("/BookStore/v1/Book");
        Assert.assertEquals(response.getStatusCode(), 204);

    }

    @Test(priority = 6)
    public void checkIfBookRemovedFromUserList() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get("/Account/v1/User/" + USER_ID);
        Assert.assertEquals(response.getStatusCode(), 200);
        jsonString = response.asString();
        List<Map<String, String>> listOfBookForUser = JsonPath.from(jsonString).getList("books");
        Assert.assertEquals(listOfBookForUser.size(), 0);
    }
}
