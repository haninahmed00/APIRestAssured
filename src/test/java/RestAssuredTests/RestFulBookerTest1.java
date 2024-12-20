package RestAssuredTests;

import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.testng.Assert;
import org.testng.annotations.Test;



public class RestFulBookerTest1 {
    @Test(description = "Create Token ")
    public void RestAssureedCreateToken() {
        //create jason body using """
        String body = """
                {
                "username" : "admin",
                 "password" : "password123"
                }
                """;
        RestAssured.
                given()
                .header("Content-Type", "application/json")
                .body(body)
                .log().all().
                when()
                .post("https://restful-booker.herokuapp.com/auth").
                then()
                .statusCode(200).
                and()
                .log().body();

    }

    @Test(description = " Create BookingID")
    public void RestAssureedCreateBookingID() {
        //create jason body using """
        String bodyCreate = """
                {
                 "firstname" : "Haneen",
                 "lastname" : "Ahmed",
                 "totalprice" : 100,
                 "depositpaid" : true,
                 "bookingdates" : {
                  "checkin" : "2024-01-02",
                   "checkout" : "2025-01-01"
                    },
                    "additionalneeds" : "Lunch"
                
                }""";

        RestAssured.
                given()
                .header("Content-Type", "application/json")
                .body(bodyCreate)
                .log().all().
                when()
                .post("https://restful-booker.herokuapp.com/booking").
                then()
                .statusCode(200).body("booking.firstname", CoreMatchers.equalTo("Haneen")).
                and()
                .log().body();
    }

    @Test(description = "GetBooking")
    public void GetBooking() {
        RestAssured.given()
                .header("Accept", "application/json")
                .log().all()
                .when()
                .get("https://restful-booker.herokuapp.com/booking/3353")
                .then()
                .statusCode(200).body("firstname", CoreMatchers.equalTo("Haneen"))
                .and()
                .log().body();
    }
    //createBookingAndBookingIdThenDeleteUser
@Test(description = "DeleteUser")
            public  void DeleteBooking(){
        String body = """
                {
                "username" : "admin",
                 "password" : "password123"
                }
                """;
        String bodyCreate = """
                {
                 "firstname" : "Hanoon",
                 "lastname" : "Awad",
                 "totalprice" : 111,
                 "depositpaid" : true,
                 "bookingdates" : {
                  "checkin" : "2024-01-02",
                   "checkout" : "2025-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                
                }
                """;
        String token =
                RestAssured.
                        given()
                        .header("Content-Type","application/json")
                        .body(body)
                        .log().all().
                        when()
                        .post("https://restful-booker.herokuapp.com/auth").
                        then()
                        .statusCode(200).
                        and()
                        .log().body().extract().jsonPath().get("token").toString();
        Assert.assertNotNull(token);





        String bookingid =

                RestAssured.
                        given()
                        .header("Content-Type","application/json")
                        .body(bodyCreate)
                        .log().all().
                        when()
                        .post("https://restful-booker.herokuapp.com/booking").
                        then()
                        .statusCode(200).body("booking.firstname",CoreMatchers.equalTo("Hanoon")).
                        and()
                        .log().body().extract().jsonPath().get("bookingid").toString();



        String responseBody =
                RestAssured.
                        given()
                        .header("Content-Type","application/json")
                        .cookie("token=<"+token+">")
                        .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=").
                        when()
                        .delete("https://restful-booker.herokuapp.com/booking/"+ bookingid).
                        then()
                        .statusCode(201).
                        and()
                        .log().body().extract().asString();

        Assert.assertTrue(responseBody.contains("Created"));

}

}