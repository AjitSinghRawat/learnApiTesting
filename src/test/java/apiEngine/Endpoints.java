package apiEngine;

import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthRequest;
import apiEngine.model.requests.RemoveBookRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Endpoints {
    public static final String BASE_URL = "https://bookstore.toolsqa.com";

    public static Response authenticateUser(AuthRequest authRequest) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("Content-Type", "application/json")
                .body(authRequest)
                .post(Route.generateToken());
        return response;
    }

    public static Response getBooks() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("content-type", "application/json")
                .get(Route.books());
        return response;
    }

    public static Response addBook(AddBooksRequest addBooksRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                //.log().all()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(addBooksRequest)
                .post(Route.books());
        return response;
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

    public static Response getUserAccount(String userId, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        Response response = request
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get(Route.userAccount(userId));
        return response;
    }
}
