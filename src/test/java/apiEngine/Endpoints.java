package apiEngine;

import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthRequest;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.response.BookListResponse;
import apiEngine.model.response.TokenResponse;
import apiEngine.model.response.UserAccount;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Endpoints {
    public static final String BASE_URL = "https://bookstore.toolsqa.com";

    public static IRestResponse<TokenResponse> authenticateUser(AuthRequest authRequest) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("Content-Type", "application/json")
                .body(authRequest)
                .post(Route.generateToken());
        //return response;
        return new RestResponse(TokenResponse.class, response);
    }

    public static IRestResponse<BookListResponse> getBooks() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("content-type", "application/json")
                .get(Route.books());
        //return response;
        return new RestResponse(BookListResponse.class, response);
    }

    public static IRestResponse<UserAccount> addBook(AddBooksRequest addBooksRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                //.log().all()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(addBooksRequest)
                .post(Route.books());
        return new RestResponse(UserAccount.class, response);
    }

    public static Response removeBook(RemoveBookRequest deleteBook, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                // .log().all()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(deleteBook)
                .delete(Route.book());
        return response;
    }

    public static IRestResponse<UserAccount> getUserAccount(String userId, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get(Route.userAccount(userId));
        //return response;
        return new RestResponse(UserAccount.class, response);
    }
}
