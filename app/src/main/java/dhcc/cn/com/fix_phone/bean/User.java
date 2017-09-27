package dhcc.cn.com.fix_phone.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author yiw
 * @ClassName: User
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午3:45:04
 */
@Table(database = AppDatabase.class)
public class User extends BaseModel {
    /**
     * FCompanyName : 18899767529
     * FContent : 估计
     * FCreateDate : 2017-08-2320: 40: 16
     * FCreatorID : 27
     * FHeadUrl : http: //120.77.202.151: 8080/OSS/GetImage?FileName=UserIcon/Default/default.png
     * FInterID : 274
     * FIsFooterData : false
     * FPhone : 18899767529
     * FShareUrl : http: //120.77.202.151: 8080/share/view/274
     * FTimeAgo : 28天前
     * FTypeID : 2
     * FTypeName : 液晶加工
     * FTypeNumber : LiquidCrystal
     * FUserName : 18899767529
     * FUserType : 1
     * FUserTypeName : 会员
     * FUserTypeNumber : VIP
     */

    @Column
    public  String  FCompanyName;

    @Column
    public  String  FContent;

    @Column
    public  String  FCreateDate;

    @Column
    public  int     FCreatorID;

    @Column
    public  String  FHeadUrl;

    @Column
    public  String  FInterID;

    @Column
    public  boolean FIsFooterData;

    @PrimaryKey
    @Column
    public  String  FPhone;

    @Column
    public  String  FShareUrl;

    @Column
    public  String  FTimeAgo;

    @Column
    public  int     FTypeID;

    @Column
    public  String  FTypeName;

    @Column
    public  String  FTypeNumber;

    @Column
    public  String  FUserName;

    @Column
    public  int     FUserType;

    @Column
    public  String  FUserTypeName;

    @Column
    public  String  FUserTypeNumber;

    @Column
    public boolean mExpand;

    public static User getUser(String FPhone) {
        return new Select().from(User.class).where(User_Table.FPhone.eq(FPhone)).querySingle();
    }

    public String getFCompanyName() {
        return FCompanyName;
    }

    public void setFCompanyName(String FCompanyName) {
        this.FCompanyName = FCompanyName;
    }

    public String getFContent() {
        return FContent;
    }

    public void setFContent(String FContent) {
        this.FContent = FContent;
    }

    public String getFCreateDate() {
        return FCreateDate;
    }

    public void setFCreateDate(String FCreateDate) {
        this.FCreateDate = FCreateDate;
    }

    public int getFCreatorID() {
        return FCreatorID;
    }

    public void setFCreatorID(int FCreatorID) {
        this.FCreatorID = FCreatorID;
    }

    public String getFHeadUrl() {
        return FHeadUrl;
    }

    public void setFHeadUrl(String FHeadUrl) {
        this.FHeadUrl = FHeadUrl;
    }

    public boolean isFIsFooterData() {
        return FIsFooterData;
    }

    public void setFIsFooterData(boolean FIsFooterData) {
        this.FIsFooterData = FIsFooterData;
    }

    public String getFPhone() {
        return FPhone;
    }

    public void setFPhone(String FPhone) {
        this.FPhone = FPhone;
    }

    public String getFShareUrl() {
        return FShareUrl;
    }

    public void setFShareUrl(String FShareUrl) {
        this.FShareUrl = FShareUrl;
    }

    public String getFTimeAgo() {
        return FTimeAgo;
    }

    public void setFTimeAgo(String FTimeAgo) {
        this.FTimeAgo = FTimeAgo;
    }

    public int getFTypeID() {
        return FTypeID;
    }

    public void setFTypeID(int FTypeID) {
        this.FTypeID = FTypeID;
    }

    public String getFTypeName() {
        return FTypeName;
    }

    public void setFTypeName(String FTypeName) {
        this.FTypeName = FTypeName;
    }

    public String getFTypeNumber() {
        return FTypeNumber;
    }

    public void setFTypeNumber(String FTypeNumber) {
        this.FTypeNumber = FTypeNumber;
    }

    public String getFUserName() {
        return FUserName;
    }

    public void setFUserName(String FUserName) {
        this.FUserName = FUserName;
    }

    public int getFUserType() {
        return FUserType;
    }

    public void setFUserType(int FUserType) {
        this.FUserType = FUserType;
    }

    public String getFUserTypeName() {
        return FUserTypeName;
    }

    public void setFUserTypeName(String FUserTypeName) {
        this.FUserTypeName = FUserTypeName;
    }

    public String getFUserTypeNumber() {
        return FUserTypeNumber;
    }

    public void setFUserTypeNumber(String FUserTypeNumber) {
        this.FUserTypeNumber = FUserTypeNumber;
    }

    public String getFInterID() {
        return FInterID;
    }

    public void setFInterID(String FInterID) {
        this.FInterID = FInterID;
    }

    public boolean isExpand() {
        return mExpand;
    }

    public void setExpand(boolean expand) {
        mExpand = expand;
    }
}
