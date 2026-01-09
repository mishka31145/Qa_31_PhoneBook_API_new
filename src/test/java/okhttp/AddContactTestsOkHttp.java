package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddContactTestsOkHttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWlraC5wYW5maWxvdnZAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Njg1NDM5NTEsImlhdCI6MTc2Nzk0Mzk1MX0.PW5KRXcbn0aO33SsNNfVuAoNezbDJWmBgnqO-Tu5C8A";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");


    @Test
    public void addNewContactSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Ron")
                .lastName("Colman")
                .email("ron"+i+"@gmail.com")
                .phone("5279325"+i)
                .address("Haifa")
                .description("Enemy")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        Assert.assertTrue(messageDTO.getMessage().contains("Contact was added! ID:"));
    }

    @Test
    public void addNewContactWrongToken() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Ron")
                .lastName("Colman")
                .email("ron@gmail.com")
                .phone("52793253582")
                .address("Haifa")
                .description("Enemy")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization","kjgvvh")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),("Unauthorized"));
    }

    @Test
    public void addNewContactWrongEmailContact() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Maya")
                .lastName("Bee")
                .email("maya1410gmail.com")
                .phone("12345561410")
                .address("Haifa")
                .description("Enemy")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDTO.getError());
        Assert.assertEquals(errorDTO.getError(),("Bad Request"));
    }

    @Test
    public void addNewContactWrongNameContact() throws IOException {
        ContactDTO contactDto = ContactDTO.builder()
                .lastName("Wolf")
                .email("jenny@mail.com")
                .address("TV")
                .phone("9876543210")
                .description("Friend").build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));
    }


}
