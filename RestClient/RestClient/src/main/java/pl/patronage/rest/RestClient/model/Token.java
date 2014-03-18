package pl.patronage.rest.RestClient.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gohilukk on 18.03.14.
 */
public class Token {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("refresh_token")
    private String refreshToken;

    public String getAccess_token() {
        return accessToken;
    }

    public void setAccessToken(String access_token) {
        this.accessToken = access_token;
    }

    public int getExpiresTn() {
        return expiresIn;
    }

    public void setExpiresIn(int expires_in) {
        this.expiresIn = expires_in;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refresh_token) {
        this.refreshToken = refresh_token;
    }
}
