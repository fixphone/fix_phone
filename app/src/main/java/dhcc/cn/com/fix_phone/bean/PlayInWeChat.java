package dhcc.cn.com.fix_phone.bean;

import dhcc.cn.com.fix_phone.base.BaseResponse;

/**
 * 2017/10/22 21
 */
public class PlayInWeChat extends BaseResponse {

    /**
     * appId : wxaba609e92f61abdf
     * partnerId : 1487628882
     * prepayId : wx201710222116101d8db446980810658509
     * nonceStr : 788B97D7AD9A49D89845E84230CB0614
     * timeStamp : 1508706970
     * sign : 7C941DD450D60948ACA2F7AB4EED2C42
     * billNo : 20171022211610296102
     */

    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String sign;
    public String billNo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
