package com.bo.utils;

import com.bo.pojo.MiaoshaUser;
import com.bo.result.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 批量生成用户，方便JMeter压测
 */
public class UserUtil {

    private void createUser(int count) throws Exception{
		List<MiaoshaUser> users = new ArrayList<MiaoshaUser>(count);
		//生成用户
		for(int i=0;i<count;i++) {
			MiaoshaUser user = new MiaoshaUser();
			user.setEmail(1000000+i+"@qq.com");
			user.setPassword("d597f12344bfcf");
			user.setUsername("user"+i);
			user.setSalt("d597f4bfcf");
			user.setRegisterDate(new Date());
			users.add(user);
		}
//		System.out.println("create user");
//		//插入数据库
//		Connection conn = DBUtil.getConn();
//		String sql = "insert into miaosha_user(email,username,password,salt,register_date)values(?,?,?,?,?)";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		for(int i=0;i<users.size();i++) {
//			MiaoshaUser user = users.get(i);
//			pstmt.setString(1,user.getEmail());
//			pstmt.setString(2, user.getUsername());
//			pstmt.setString(3, user.getPassword());
//			pstmt.setString(4, user.getSalt());
//			pstmt.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.addBatch();
//		}
//		pstmt.executeBatch();
//		pstmt.close();
//		conn.close();
//		System.out.println("insert to db");
		//登录，生成token
		String urlString = "http://localhost:8080/user/login";
		File file = new File("E:/tokens.txt");
		if(file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		file.createNewFile();
		raf.seek(0);
		for(int i=0;i<users.size();i++) {
			MiaoshaUser user = users.get(i);
			URL url = new URL(urlString);
			HttpURLConnection co = (HttpURLConnection)url.openConnection();
			co.setRequestMethod("POST");
			co.setDoOutput(true);
			OutputStream out = co.getOutputStream();
			String params = "email="+user.getEmail()+"&password=1234";
			out.write(params.getBytes());
			out.flush();
			InputStream inputStream = co.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0 ,len);
			}
			inputStream.close();
			bout.close();
			String response = new String(bout.toByteArray());
            R r = new ObjectMapper().readValue(response, R.class);
            Map<String, Object> data = r.getData();
            String token = (String) data.get("token");
			String row = user.getEmail()+","+token;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\r\n".getBytes());
		}
		raf.close();

		System.out.println("over");
	}
	
	public static void main(String[] args)throws Exception {
		new UserUtil().createUser(2000);
	}
}
