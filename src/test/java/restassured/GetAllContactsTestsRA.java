package restassured;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.GetAllContactsDTO;
import io.restassured.RestAssured;
import io.restassured.specification.Argument;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class GetAllContactsTestsRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWlraC5wYW5maWxvdnZAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Njg1NDM5NTEsImlhdCI6MTc2Nzk0Mzk1MX0.PW5KRXcbn0aO33SsNNfVuAoNezbDJWmBgnqO-Tu5C8A";
    String endpoint = "contacts";

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void getAllContactsSuccess() {
        GetAllContactsDTO contactsDTO = given()
                .header("Authorization",token)
                .when()
                .get(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDTO.class);

        List<ContactDTO>list = contactsDTO.getContacts();
        for(ContactDTO c:list) {
            System.out.println(c.getId());
            System.out.println(c.getEmail());
            System.out.println("===============================");
        }
        System.out.println("Size of List --> " + list.size());

    }

    @Test
    public void getAllContactsWrongToken() {
       ErrorDTO errorDTO = given()
                .header("Authorization","kjgv")
                .when()
                .get(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Unauthorized");
        Assert.assertTrue(errorDTO.getMessage().toString().contains("JWT strings must contain"));
    }

    @Test
    public void getAllContactsWrongToken2() {
                    given()
                .header("Authorization","kjgv")
                .when()
                .get(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("JWT strings must contain"))
                .assertThat().body("error", equalTo("Unauthorized"));
    }



}
