import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.net.ssl.HttpsURLConnection;

import data.jdbc.*;
import data.model.Animal;


public class UpdateDB {
	private static JDBCInfo x = new JDBCInfo("./jdbcInfo.xml");

	public static void main(String[] args) {
		parseAnimalJson();
		
		parseAnimalHospitalJson();
	}
	
	private static void parseAnimalHospitalJson() {
		String tableName = "hospital";
		String id = "id";
		String name = "name";
		String tel = "tel";
		String address = "addr";
		int count = 0;
		String value1 = "";
		String value2 = "";
		String value3 = "";
		JDBCMSSQL jdbc = null;

		try {
			jdbc = new JDBCMSSQL(x.getDriver(), x.getDBServerIP(), x.getDBServerPort(), x.getDBUserName(), x.getDBPassword());	
			jdbc.createConnection(x.getDatabase());

			FileWriter fw = new FileWriter(".\\NewTaipeiCityAnimalHospital.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			InputStream is = new FileInputStream(".\\NewTaipeiCityAnimalHospital.json");
			JsonParser parser = Json.createParser(is);
			while (parser.hasNext()) {
				Event e = parser.next();
				if (e == Event.KEY_NAME) {
					switch (parser.getString()) {
					case "name":
						parser.next();
						value1 = parser.getString();
						writeToFile(bw, "name: " + value1);
						count++;
						break;
					case "Tel":
						parser.next();
						value2 = parser.getString();
						writeToFile(bw, "Tel: " + value2);
						break;
					case "address":
						parser.next();
						value3 = parser.getString();
						writeToFile(bw, "address: " + value3);
						jdbc.executeUpdate("insert into " + tableName + "(" + id
								+ "," + name + "," + tel + "," + address
								+ ") values(" + count + ",'" + value1 + "','"
								+ value2 + "','" + value3 + "')");
						break;
					}
				}
			}
			jdbc.commit();
			is.close();
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		} finally {
			if(jdbc.getConn() != null) {
				try {
					jdbc.connectionClose();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		System.out.println("Parse successful...");
	}
	
	private static void parseAnimalJson() {

		int count = 1;
		ArrayList<Animal> animals = new ArrayList<Animal>();
		try {
			SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat datetimef = new SimpleDateFormat("yyyy/MM/dd");
			FileWriter fw = new FileWriter(".\\AnimalOpenData.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			//若要使用雲端open data更新資料，請使用以下4行
			String httpsURL = "https://data.coa.gov.tw/Service/OpenData/TransService.aspx?UnitId=QcbUEzN6E6DL";
			URL url = new URL(httpsURL);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			InputStream is = conn.getInputStream();

			//若要使用下載到local端的open data更新資料，請使用以下1行
//			InputStream is = new FileInputStream(".\\AnimalOpenData.json");
					
			JsonReader jsonReader = Json.createReader(is);
			JsonArray jArray = jsonReader.readArray();
			int s = jArray.size();
			System.out.println("共有"+ s + "筆資料");
			for(JsonValue jObject : jArray) {
				Animal animal = new Animal();
				writeToFile(bw, "第" + count + "筆:");
				JsonObject o = (JsonObject)jObject;
				Set<String> oks = o.keySet();
				for(String str : oks) {
					switch (str) {
					case "animal_id":
						String check_id = o.get(str).toString();
						writeToFile(bw, "animal_id: " + check_id);
						animal.setCheck_id(check_id);
						break;
					case "animal_subid":
						String sub_id = o.getString(str);
						writeToFile(bw, "animal_subid: " + sub_id);
						animal.setSub_id(sub_id);
						break;
					case "animal_area_pkid":
						String area_id = o.get(str).toString();
						writeToFile(bw, "animal_area_pkid: " + area_id);
						animal.setArea(area_id);
						break;	
					case "animal_shelter_pkid":
						int shelter_id = Integer.parseInt(o.get(str).toString());
						writeToFile(bw, "animal_shelter_pkid: " + shelter_id);
						animal.setShelter_id(shelter_id);
						break;	
					case "animal_place":
						String place = o.getString(str);
						writeToFile(bw, "animal_place: " + place);
						animal.setPlace(place);
						break;	
					case "animal_kind":
						String kind = o.getString(str);
						writeToFile(bw, "animal_kind: " + kind);
						animal.setKind(kind);
						break;	
					case "animal_sex":
						String sex = o.getString(str);
						writeToFile(bw, "animal_sex: " + sex);
						animal.setSex(sex);
						break;	
					case "animal_bodytype":
						String bodyType = o.getString(str);
						writeToFile(bw, "animal_bodytype: " + bodyType);
						animal.setBodyType(bodyType);
						break;	
					case "animal_colour":
						String colour = o.getString(str);
						writeToFile(bw, "animal_colour: " + colour);
						animal.setColour(colour);
						break;	
					case "animal_age":
						String age = o.getString(str);
						writeToFile(bw, "animal_age: " + age);
						animal.setAge(age);
						break;	
					case "animal_sterilization":
						String sterilization = o.getString(str);
						writeToFile(bw, "animal_sterilization: " + sterilization);
						animal.setSterilization(sterilization);
						break;
					case "animal_bacterin":
						String bacterin = o.getString(str);
						writeToFile(bw, "animal_bacterin: " + bacterin);
						animal.setBacterin(bacterin);
						break;
					case "animal_foundplace":
						String foundPlace = o.getString(str);
						writeToFile(bw, "animal_foundplace: " + foundPlace);
						animal.setFoundPlace(foundPlace);
						break;
					case "animal_title":
						writeToFile(bw, "animal_title: " + o.getString(str));
						break;
					case "animal_status":
						String status = o.getString(str);
						writeToFile(bw, "animal_status: " + status);
						animal.setStatus(status);
						break;
					case "animal_remark":
						String remark = replaceStr(o.getString(str));
						writeToFile(bw, "animal_remark: " + remark);
						animal.setRemark(remark);
						break;
					case "animal_caption":

						writeToFile(bw, "animal_caption: " + o.getString(str));
						break;
					case "animal_opendate":
						Date openDate;
						if(!o.getString(str).equals("")) {
							openDate = datef.parse(o.getString(str));
						}
						else {
							openDate = null;
						}
						writeToFile(bw, "animal_opendate: " + (openDate == null ? "" : openDate));
						animal.setOpenDate(openDate);
						break;
					case "animal_closeddate":
						Date closedDate;
						if(!o.getString(str).equals("")) {
							closedDate = datef.parse(o.getString(str));
						}
						else {
							closedDate = null;
						}						
						writeToFile(bw, "animal_closeddate: " + (closedDate == null ? "" : closedDate));
						animal.setClosedDate(closedDate);
						break;
					case "animal_update":
						Date upDate;
						if(!o.getString(str).equals("")) {
							upDate = datetimef.parse(o.getString(str));
						}
						else {
							upDate = null;
						}
						writeToFile(bw, "animal_update: " + (upDate == null ? "" : upDate));
						animal.setUpDate(upDate);
						break;
					case "animal_createtime":
						Date createDate;
						if(!o.getString(str).equals("")) {
							createDate = datetimef.parse(o.getString(str));
						}
						else {
							createDate = null;
						}
						writeToFile(bw, "animal_createtime: " + (createDate == null ? "" : createDate));
						animal.setCreateDate(createDate);
						break;
					case "shelter_name":
						String shelter_name = o.getString(str);
						writeToFile(bw, "shelter_name: " + shelter_name);
						animal.setShelter_name(shelter_name);
						break;
					case "album_update":
						writeToFile(bw, "album_update: " + o.getString(str));
						break;
					case "album_file":
						String album_file = o.getString(str);
						if(album_file.equals("")) {
							album_file = "img2/no-image-available.jpg";
						}
						writeToFile(bw, "album_file: " + album_file);
						animal.setAlbum(album_file);
						break;
					case "shelter_address":
						String shelter_address = o.getString(str);
						writeToFile(bw, "shelter_address: " + shelter_address);
						animal.setShelter_address(shelter_address);
						break;
					case "shelter_tel":
						String shelter_tel = o.getString(str);
						writeToFile(bw, "shelter_tel: " + shelter_tel);
						animal.setShelter_tel(shelter_tel);
						break;
					}
				}
				animals.add(animal);
				count++;
			}
			setToModle(animals);
			bw.flush();
			bw.close();
			fw.close();
			is.close();
		}
		catch (MalformedURLException e2) {
			System.out.println(e2.getMessage());
		}
		catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		System.out.println("Parse successful...");
	}

	private static void writeToFile(BufferedWriter bw, String str) throws IOException {
		bw.append(str);
		bw.newLine();
	}

	private static void setToModle(ArrayList<Animal> animals) {
		
		JDBCMSSQL jdbc = null;
		//SimpleDateFormat datetimef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat datetimef = new SimpleDateFormat("yyyy-MM-dd");
		String sqlformat = "exec sp_insert_animal '%s', '%s', %d, '%s', '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s',"
				+ " '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'";
		try {
			jdbc = new JDBCMSSQL(x.getDriver(), x.getDBServerIP(), x.getDBServerPort(), x.getDBUserName(), x.getDBPassword());	
			jdbc.createConnection(x.getDatabase());
			for (Animal animal : animals) {
				String sqlString = String.format(sqlformat, 
						animal.getCheck_id(), 
						animal.getSub_id(),
						animal.getShelter_id(),
						animal.getShelter_name(),
						animal.getPlace(),
						Integer.parseInt(animal.getArea()),
						animal.getKind(),
						animal.getSex(),
						animal.getBodyType(),
						animal.getColour(),
						animal.getAge(),
						animal.getSterilization(),
						animal.getBacterin(),
						animal.getFoundPlace(),
						animal.getStatus(),
						animal.getRemark(),
						animal.getOpenDate() == null ? "" : datetimef.format(animal.getOpenDate()),
						animal.getClosedDate() == null ? "" : datetimef.format(animal.getClosedDate()),
						animal.getCreateDate() == null ? "" : datetimef.format(animal.getCreateDate()),
						animal.getUpDate() == null ? "" : datetimef.format(animal.getUpDate()),
						animal.getAlbum(),
						animal.getShelter_address(),
						animal.getShelter_tel());
				jdbc.executeUpdate(sqlString);
			}						
			jdbc.commit();
			System.out.println("資料匯入成功...");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			if(jdbc.getConn() != null) {
				try {
					jdbc.connectionClose();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static String replaceStr(String str) {
		str = str.replaceAll("'", "");
		str = str.replaceAll("\"", "");
		StringBuilder ss = new StringBuilder(str);
		int start = str.indexOf("<");
		int end = str.lastIndexOf(">");
		if(start >= 0 && end >= 0 && end > start) {
			ss.replace(str.indexOf("<"), str.lastIndexOf(">")+1, "");
		}

		start = str.indexOf("&");
		end = str.lastIndexOf(";");		
		if(start >= 0 && end >= 0 && end > start) {
			ss.replace(str.indexOf("&"), str.lastIndexOf(";")+1, "");
		}

		return ss.toString();
	}
}