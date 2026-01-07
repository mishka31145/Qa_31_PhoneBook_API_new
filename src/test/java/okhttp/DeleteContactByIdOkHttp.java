package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.MessageDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIdOkHttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWlraC5wYW5maWxvdnZAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NjgzODAwOTIsImlhdCI6MTc2Nzc4MDA5Mn0.9TP4RJxFd74yMKCAqPpb7mE_kTGUY24aDt-w2WocEao";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String id;
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Maya")
                .lastName("Bee")
                .email("maya"+i+"@gmail.com")
                .phone("1234556"+i)
                .address("Haifa")
                .description("Bee")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        String message = messageDTO.getMessage(); //Contact was added! ID: .....
        System.out.println(message);
        String[] all = message.split(": ");
        id = all[1];
        System.out.println(id);
        //get id from "message"
        //id=""

    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
        MessageDTO dto = gson.fromJson(response.body().string(), MessageDTO.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(),"Contact was deleted!");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/636203af-8da5-4eb8-a7ae-a53d372eba83")
                .delete()
                .addHeader("Authorization","khjgv")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Unauthorized");
    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+123)
                .delete()
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDTO.getError());
        Assert.assertEquals(errorDTO.getError(),"Bad Request");
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(),"Contact with id: 123 not found in your contacts!");
    }

}

//514f6bcd-7b7c-486c-9663-b7434b69b037
//wow1914@gmail.com
//================================================
//636203af-8da5-4eb8-a7ae-a53d372eba83
//wow1697@gmail.com
//================================================
//7d5cff8b-d878-42fd-a246-99ba0a02fd0b
//wow1049@gmail.com
