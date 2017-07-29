package endpointApiTests;

import android.content.res.Resources;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.humaniq.apilib.BuildConfig;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.R;
import com.humaniq.apilib.network.models.request.ValidateRequest;
import com.humaniq.apilib.network.models.request.profile.AccountPassword;
import com.humaniq.apilib.network.models.request.profile.AccountPerson;
import com.humaniq.apilib.network.models.request.profile.UserId;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.models.response.FacialImage;
import com.humaniq.apilib.network.models.response.FacialImageValidation;
import com.humaniq.apilib.network.models.response.ValidationResponse;
import com.humaniq.apilib.network.models.response.contacts.ContactsResponse;
import com.humaniq.apilib.network.models.response.profile.AccountProfile;
import com.humaniq.apilib.network.models.response.profile.DeauthErrorModel;
import com.humaniq.apilib.network.models.response.profile.DeauthModel;
import com.humaniq.apilib.network.service.ContactService;
import com.humaniq.apilib.network.service.ProfileService;
import com.humaniq.apilib.network.service.ValidationService;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

/**
 * Created by gritsay on 7/20/17.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ProfileApiTest {

  @Before
  public void init() {
    ShadowLog.stream = System.out;
  }

  @Test public void deauthenticateUserRightResponse(){
    try {
      ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);
      ProfileService apiEndpoints = ServiceBuilder.getProfileService();

      Call<DeauthModel> call = apiEndpoints.deauthenticateUser(new UserId(""));
      Response<DeauthModel> response = call.execute();
      DeauthModel extractContactResponse = response.body();
      System.out.println(response.errorBody().string());
      //ShadowLog.v("tag", response.errorBody().string());
      assertTrue(response.isSuccessful());

  } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Test public void testUpdateUserPerson() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);

    try {
      ProfileService service = ServiceBuilder.getProfileService();

      AccountPerson accountPerson = new AccountPerson();
      accountPerson.setAccountId("1569731856058811465");
      AccountPerson.Person person = new AccountPerson.Person();
      person.setFirstName("Anton");
      person.setLastName("Mozgovoy");
      accountPerson.setPerson(person);

      Call<BasePayload<AccountPerson>> call = service.updateAccountPerson(accountPerson);

      Response<BasePayload<AccountPerson>> response = call.execute();

      BasePayload<AccountPerson> baseResponse = response.body();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testUpdateAccountPassword() {
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);

    try {
      ProfileService service = ServiceBuilder.getProfileService();

      AccountPassword accountPassword = new AccountPassword();
      accountPassword.setAccountId("1568161709003113564");
      accountPassword.setOldPassword("1234567");
      accountPassword.setNewPassword("1234567");
      //AccountPerson.Person person = new AccountPerson.Person();
      //person.setFirstName("Anton");
      //person.setLastName("Mozgovoy");
      //accountPerson.setPerson(person);

      Call<BasePayload<Object>> call = service.updateAccountPassword(accountPassword);

      Response<BasePayload<Object>> response = call.execute();

      BasePayload<Object> baseResponse = response.body();

      assertTrue(response.isSuccessful());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test public void testUpdateAccountAvatar() {

  }

  @Test public void testGetAccountProfile() {
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);

    try {
      ProfileService service = ServiceBuilder.getProfileService();
      Call<BasePayload<AccountProfile>> call = service.getAccountProfile("1567498755333161994");
      Response<BasePayload<AccountProfile>> payload = call.execute();
      Log.d("profile", payload.body().payload.getPerson().getFirstName());

    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  String facialImageId;
  @Test public void testValidationSteps() {
    new Prefs(RuntimeEnvironment.application);
    ServiceBuilder.init(Constants.CONTACTS_BASE_URL, RuntimeEnvironment.application);

    try {
      Resources res = RuntimeEnvironment.application.getResources();
      InputStream in_s = res.openRawResource(R.raw.ava);

      byte[] b = new byte[in_s.available()];
      in_s.read(b);
      base64 = new String(b);
    } catch (Exception e) {
      // e.printStackTrace();
    }

    try {

    ValidationService validationService = ServiceBuilder.getValidationService();


    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("facial_image", base64);
    Call<BasePayload<FacialImage>> call = validationService.isRegistered(jsonObject);
      Response<BasePayload<FacialImage>> payloadResponse = call.execute();
      facialImageId = payloadResponse.body().payload.getFacialImageId();
      JsonObject jsonObject1 = new JsonObject();
      jsonObject1.addProperty("facial_image_id", facialImageId);
      Call<BasePayload<FacialImageValidation>> call1 = validationService.createValidation(jsonObject1);
      Response<BasePayload<FacialImageValidation>> payloadResponse1 = call1.execute();

      ValidateRequest validateRequest = new ValidateRequest();
      validateRequest.setFacialImageId(payloadResponse1.body().payload.getFacialImageValidationId());
      validateRequest.setFacialImage(base64);
      Call<BasePayload<ValidationResponse>> call2 = validationService.validate(validateRequest);
      Response<BasePayload<ValidationResponse>> payloadResponse2 = call2.execute();

      System.out.println(payloadResponse2.body().payload.getMessage());
      assertTrue(payloadResponse2.isSuccessful());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private String base64;


}
