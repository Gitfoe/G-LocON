package signaling_server;

public class UserInfo {

	//メンバ変数
    private String publicIP;
    private int publicPort;
    private String privateIP;
    private int privatePort;
    private double latitude;
    private double longitude;
    private String peerId;
    //private double speed;


    //コンストラクタ（引数なし）
    public UserInfo() {
    }
/*
    public UserInfo(String publicIP,int publicPort,String privateIP,int privatePort,double latitude,double longitude,String peerId){
        this.publicIP = publicIP;
        this.publicPort = publicPort;
        this.privateIP = privateIP;
        this.privatePort = privatePort;
        this.latitude = latitude;
        this.longitude = longitude;
        this.peerId = peerId;
    }
    */
    public UserInfo(String publicIP,int publicPort,String privateIP,int privatePort,double latitude,double longitude,String peerId){
        this.publicIP = publicIP;
        this.publicPort = publicPort;
        this.privateIP = privateIP;
        this.privatePort = privatePort;
        //this.speed = speed;
        this.latitude = latitude;
        this.longitude = longitude;
        this.peerId = peerId;
    }


	/**
	 * @return publicIP
	 */
	public String getPublicIP() {
		return publicIP;
	}

	/**
	 * @param publicIP セットする publicIP
	 */
	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}

	/**
	 * @return publicPort
	 */
	public int getPublicPort() {
		return publicPort;
	}

	/**
	 * @param publicPort セットする publicPort
	 */
	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	/**
	 * @return privateIP
	 */
	public String getPrivateIP() {
		return privateIP;
	}

	/**
	 * @param privateIP セットする privateIP
	 */
	public void setPrivateIP(String privateIP) {
		this.privateIP = privateIP;
	}

	/**
	 * @return privatePort
	 */
	public int getPrivatePort() {
		return privatePort;
	}

	/**
	 * @param privatePort セットする privatePort
	 */
	public void setPrivatePort(int privatePort) {
		this.privatePort = privatePort;
	}

	/**
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude セットする latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude セットする longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
/*
	//speed
	public double getSpeed() {
		return speed;
	}

	//speedのセット
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	*/
	/**
	 * @return peerId
	 */
	public String getPeerId() {
		return peerId;
	}
	/**
	 * @param peerId セットする peerId
	 */
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}


}
