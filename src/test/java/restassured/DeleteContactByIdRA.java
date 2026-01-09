package restassured;

import dto.ContactDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteContactByIdRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWlraC5wYW5maWxvdnZAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Njg1NDM5NTEsImlhdCI6MTc2Nzk0Mzk1MX0.PW5KRXcbn0aO33SsNNfVuAoNezbDJWmBgnqO-Tu5C8A";
    String endpoint = "contacts";
    String id;

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Dowww")
                .email("donna"+i+"@gmail.com")
                .phone("1234567"+i)
                .address("TA")
                .description("Friend")
                .build();

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
        //Contact was added! ID: .....
                .extract()
                .path("message");
        String[]all = message.split(" ");
        id = all[4];

    }

    @Test
    public void deleteContactByIdSuccess() {
        MessageDTO messageDTO = given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(MessageDTO.class);
        Assert.assertEquals(messageDTO.getMessage(),("Contact was deleted!"));
    }

    @Test
    public void deleteContactByIdSuccess2() {
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Contact was deleted!"));
    }

    @Test
    public void deleteContactByIdWrongToken() {
        given()
                .header("Authorization","jhg")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401);
    }

}
