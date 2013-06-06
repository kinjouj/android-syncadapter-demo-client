package kinjouj.sample.authadapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;

import com.google.gson.internal.LinkedHashTreeMap;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.api.Scope;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@EBean(scope = Scope.Singleton)
public class Service {

    private RestTemplate restTemplate;

    @StringRes
    public String restServerUrl;

    @Bean
    public OAuth oauth;

    public Service(Context context) {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
    }

    public List<Sample> getSamples(final String accessToken) {
        List<Sample> samples = new ArrayList<Sample>();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setAuthorization(
                new HttpAuthentication() {
                    @Override
                    public String getHeaderValue() {
                        return "Bearer " + accessToken;
                    }
                }
            );

            HttpEntity<Object> entity = new HttpEntity<Object>(headers);

            ResponseEntity<Sample[]> response = restTemplate.exchange(
                restServerUrl.concat("/api/sample"),
                HttpMethod.GET,
                entity,
                Sample[].class
            );

            samples.addAll(Arrays.asList(response.getBody()));
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return samples;
    }

    @SuppressWarnings("unchecked")
    public String getAccessToken(String username, String password) {
        String token = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("client_id", oauth.clientId);
            params.add("client_secret", oauth.clientSecret);
            params.add("username", username);
            params.add("password", password);
            params.add("grant_type", "password");

            HttpEntity<Object> entity = new HttpEntity<Object>(params, headers);
            ResponseEntity<Object> response = restTemplate.exchange(
                restServerUrl.concat("/oauth/token"),
                HttpMethod.POST,
                entity,
                Object.class
            );

            Object body = response.getBody();

            if (body instanceof LinkedHashTreeMap) {
                LinkedHashTreeMap<Object, Object> tokens = 
                    (LinkedHashTreeMap<Object, Object>)body;

                if (tokens.containsKey("access_token")) {
                    token = (String)tokens.get("access_token");
                }
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return token;
    }
}