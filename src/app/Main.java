package app;

import common.util.DBUtil;

public class Main {

	public static void main(String[] args) {
		System.out.println("🚀 프로그램 시작");
		DBUtil.testConnection();
		System.out.println("🏁 프로그램 종료");
	}

}
