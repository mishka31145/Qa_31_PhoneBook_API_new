package restassured;

import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class AddNewContactTestsRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWlraC5wYW5maWxvdnZAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Njg1NDM5NTEsImlhdCI6MTc2Nzk0Mzk1MX0.PW5KRXcbn0aO33SsNNfVuAoNezbDJWmBgnqO-Tu5C8A";
    String endpoint = "contacts";

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void addNewContactSuccess() {
        int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Dowww")
                .email("donna"+i+"@gmail.com")
                .phone("1234567"+i)
                .address("TA")
                .description("Friend")
                .build();

       MessageDTO message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(MessageDTO.class);
        System.out.println(message);
        Assert.assertTrue(message.getMessage().contains("Contact was added! ID:"));



    }

    @Test
    public void addNewContactWrongName() {
        ContactDTO contactDTO = ContactDTO.builder()
                .lastName("Dowww")
                .email("donna@gmail.com")
                .phone("123456723454")
                .address("TA")
                .description("Friend")
                .build();

        ErrorDTO error = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .extract()
                .response()
                .as(ErrorDTO.class);
        System.out.println(error);
        Assert.assertTrue(error.getMessage().toString().contains("name=must not be blank"));

    }

}
