package restassured;

import dto.ContactDTO;
import dto.MessageDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateContactTestsRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWlraC5wYW5maWxvdnZAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Njg1NDM5NTEsImlhdCI6MTc2Nzk0Mzk1MX0.PW5KRXcbn0aO33SsNNfVuAoNezbDJWmBgnqO-Tu5C8A";

    String id;
    ContactDTO contactDTO = ContactDTO.builder()
            .name("Donna")
            .lastName("Dowww")
            .email("donna@gmail.com")
            .phone("123456745673")
            .address("TA")
            .description("Friend")
            .build();

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");

        String[]all = message.split(": ");
        id = all[1];
    }

    @Test
    public void UpdateExistsContactSuccess() {
        String name = contactDTO.getName();
        contactDTO.setId(id);
        contactDTO.setName("wwwwww");

        given()
                .body(contactDTO)
                .header("Authorization",token)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));

    }



}
