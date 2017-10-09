package dhcc.cn.com.fix_phone.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrator on 2017/10/9 0009.
 *
 */

@Table(database = AppDatabase.class)
public class LoginInfo extends BaseModel {

    @PrimaryKey
    @Column
    public String userID;

    @Column
    public String phone;

    @Column
    public String accessToken;

    @Column
    public String refreshToken;

    @Column
    public String expireIn;

    @Override
    public String toString() {
        return "LoginBody{" +
                "userID='" + userID + '\'' +
                ", phone='" + phone + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expireIn='" + expireIn + '\'' +
                '}';
    }

    public static LoginInfo getLoginInfo(String userID) {
        return new Select().from(LoginInfo.class).where(LoginInfo_Table.userID.eq(userID)).querySingle();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }
}
