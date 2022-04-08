package com.respond;

public class ExampleRespond {
    private final String userName;
	private final String userState;
    private final String userLevel;

	public ExampleRespond(String N, String S, String L) {
		this.userName = N;
		this.userState = S;
        this.userLevel = L;
	}

    public String getUserName() {
		return userName;
	}

    public String getUserState() {
		return userState;
	}

    public String getUserLevel() {
		return userLevel;
	}
}
