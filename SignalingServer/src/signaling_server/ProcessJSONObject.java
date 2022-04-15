package signaling_server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProcessJSONObject {
	private JSONObject jsonObject;

	//コンストラクタ（引数なし）
	public ProcessJSONObject() {

	}

	//コンストラクタ（引数あり）
    ProcessJSONObject(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    /**
     * 処理タイプを受信データから取得する
     * @return//処理タイプ
     */
    public String getProcessType(){
        String processType = "";
        try {
            processType = jsonObject.getString("processType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return processType;
    }

    /**
     * ユーザ情報を成形し返却する
     * @return 送信元のUserInfo
     */
    public UserInfo getUserInfo(){
        UserInfo userInfo = new UserInfo();
        try {
            userInfo.setPublicIP(jsonObject.getString("publicIP"));
            userInfo.setPublicPort(jsonObject.getInt("publicPort"));
            userInfo.setPrivateIP(jsonObject.getString("privateIP"));
            userInfo.setPrivatePort(jsonObject.getInt("privatePort"));
            userInfo.setLatitude(jsonObject.getDouble("latitude"));
            userInfo.setLongitude(jsonObject.getDouble("longitude"));
            userInfo.setPeerId(jsonObject.getString("peerID"));
            //userInfo.setSpeed(jsonObject.getDouble("speed"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /**
     * jsonから検索範囲を取得する
     * @return//検索範囲
     */
    public double getSearchDistance(){
        double searchDistance = 0.0;
        try {
            searchDistance = jsonObject.getDouble("searchDistance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchDistance;
    }

    /**
     * UserInfos（ユーザ情報リスト）をJSONObjectにする
     * @Parm 周辺ユーザ情報
     * @return 形成したJSONObject
     */
    public JSONObject getUserInfoList(ArrayList<UserInfo> userInfos){
        JSONObject jsonObject = new JSONObject();
        JSONArray userList = new JSONArray();

        for(int i = 0; i < userInfos.size(); i++){
            UserInfo userInfo = userInfos.get(i);
            JSONObject user = new JSONObject();
            try {
                user.put("publicIP",userInfo.getPublicIP());
                user.put("publicPort",userInfo.getPublicPort());
                user.put("privateIP",userInfo.getPrivateIP());
                user.put("privatePort",userInfo.getPrivatePort());
                user.put("latitude",userInfo.getLatitude());
                user.put("longitude",userInfo.getLongitude());
                user.put("peerID",userInfo.getPeerId());
                //user.put("speed", userInfo.getSpeed());
                userList.put(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObject.put("processType","getPeripheralUserInfoList");
            jsonObject.put("userList",userList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 送信元ユーザ情報をJSONObjectにする
     * @param userInfo 送信元ユーザ情報
     * @return userInfoをJSONにしたもの
     */
    public JSONObject getSrcUserInfo(UserInfo userInfo){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType","doUDPHolePunching");
            jsonObject.put("publicIP",userInfo.getPublicIP());
            jsonObject.put("publicPort",userInfo.getPublicPort());
            jsonObject.put("privateIP",userInfo.getPrivateIP());
            jsonObject.put("privatePort",userInfo.getPrivatePort());
            jsonObject.put("latitude",userInfo.getLatitude());
            jsonObject.put("longitude",userInfo.getLongitude());
            jsonObject.put("peerID",userInfo.getPeerId());
            //jsonObject.put("speed",userInfo.getSpeed());
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return jsonObject;
    }




}
