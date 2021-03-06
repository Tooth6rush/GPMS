package com.find1x.gpms.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import com.find1x.gpms.pojos.Student;
import com.find1x.gpms.pojos.Teacher;
import com.find1x.gpms.pojos.User;
import com.find1x.gpms.util.MongoDBUtil;

public class StudentDAO {
	public static List<Student> getList() {
		List<Student> list = MongoDBUtil.getDatastore().find(Student.class).order("no")
				.asList();
		return list;
	}

	public static Student getStudentInfo(String no) {
		Student student = MongoDBUtil.getDatastore().find(Student.class)
				.filter("no", no).get();
		return student;
	}
	
	public static Teacher getTeacherInfo(String no){
		Student student = MongoDBUtil.getDatastore().find(Student.class)
				.filter("no", no).get();
		Teacher teacher=TeacherDAO.getTeacherInfo(student.getTeacher());
		return teacher;
	}
	
	public static ObjectId addStudent(String no, String name, String sex,
			String classno, String department, String specialty,
			String telephone, String email) {
		try {
			Student student = new Student();
			student.setNo(no);
			student.setName(name);
			student.setSex(sex);
			student.setClassno(classno);
			student.setDepartment(department);
			student.setSpecialty(specialty);
			student.setTelephone(telephone);
			student.setEmail(email);
			MongoDBUtil.getDatastore().save(student);
			return MongoDBUtil.getDatastore().find(Student.class)
					.filter("no", no).get().get_id();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean createUser(ObjectId _id, String username) {
		try {
			User user = new User();
			user.setRefId(_id);
			user.setUsername(username);
			user.setType(0);
			user.setPassword("123456");
			MongoDBUtil.getDatastore().save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean existStudent(String no) {
		if (MongoDBUtil.getDatastore().find(Student.class).filter("no", no)
				.get() == null)
			return false;
		else
			return true;
	}

	public static boolean addSubject(String no, String firstChoice,
			String secondChoice, String thirdChoice) {
		try {
			String teacher;
			Datastore ds = MongoDBUtil.getDatastore();
			ds.update(
					ds.find(Student.class).filter("no", no),
					ds.createUpdateOperations(Student.class)
							.set("firstChoice", firstChoice)
							.set("secondChoice", secondChoice)
							.set("thirdChoice", thirdChoice));
			if((teacher=IssueDAO.selectSubject(firstChoice))!=null){
				ds.update(
						ds.find(Student.class).filter("no", no),
						ds.createUpdateOperations(Student.class)
								.set("teacher", teacher).set("issue", firstChoice));
			}else if((teacher=IssueDAO.selectSubject(secondChoice))!=null){
				ds.update(
						ds.find(Student.class).filter("no", no),
						ds.createUpdateOperations(Student.class)
								.set("teacher", teacher).set("issue", secondChoice));
			}else if((teacher=IssueDAO.selectSubject(thirdChoice))!=null){
				ds.update(
						ds.find(Student.class).filter("no", no),
						ds.createUpdateOperations(Student.class)
								.set("teacher", teacher).set("issue", thirdChoice));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String hasIssue(String no){
		Student student=MongoDBUtil.getDatastore().find(Student.class).filter("no", no).get();
		if(student.getIssue()!=null){
			return student.getIssue();
		}else{
			return null;
		}
	}
}
