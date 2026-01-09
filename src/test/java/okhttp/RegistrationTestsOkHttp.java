package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrationTestsOkHttp {

    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void registrationSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("mikh.panfilovv"+i+"@gmail.com")
                .password("Mixan31145$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        String token = responseDTO.getToken();
    }

    @Test
    public void registrationWrongEmail() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("mikh.panfilovvgmail.com")
                .password("Mixan31145$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Bad Request");

    }

    @Test
    public void registrationWrongPassword() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("mikh.panfilovv@gmail.com")
                .password("Mixan31")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Bad Request");

    }

    @Test
    public void registrationUserAlreadyExists() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("mikh.panfilovv@gmail.com")
                .password("Mixan31145$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getMessage(),"User already exists");
        Assert.assertEquals(errorDTO.getError(),"Conflict");

    }






}
