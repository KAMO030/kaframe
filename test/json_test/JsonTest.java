package json_test;


import com.kamo.core.util.JsonParser;
import json_test.pojo.Human;
import json_test.pojo.Student;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {
    public static void main(String[] args) {
        ArrayList<Human> human = new ArrayList<>();
        human.add(new Human().setAge(32).setName("zs"));
        List<List<Integer>> newStudents = new ArrayList<>();
        List<String>newStudent  = new ArrayList<>();
        newStudent.add("1");
        newStudent.add("2");
        newStudent.add("3");

        Student student1 = new Student().setStudent(true).setStuId("123123").setHumans(human).setNewStudents(newStudent.toArray(new String[0]));
        student1.setName("haha").setAge(12);
//        String json ="{ stuId : 123123 , isStudent : true   , humans : [ { name: zs, age:32 } ] , newStudents:[  \"1\" ,  \"2\" ,\"3\" ] , name : haha , age:12 }";
//        System.out.println(json);
//        List<Student> list = new ArrayList<>();
//        list.add(student1);
//        list.add(student1);
//        list.add(student1);
//        String json = JsonHelper.object2JSON(student1);
//        System.out.println(json);
//        Student student = JsonHelper.json2Object(json, Student.class);
//        System.out.println(student);
        String json = JsonParser.object2JSON(student1);
        System.out.println(json);
//        String[] student = JsonHelper.json2Object(json, String[].class);
//
//        System.out.println(Arrays.toString(student));
        Student student = JsonParser.json2Object(json, Student.class);
        System.out.println(student);
//        System.out.println(student);
//        {name=123, cinfoDao={arg={name=123, cinfoDao={arg=123, id=123}}, id=123}}
//        {name=123, cinfoDao={arg={name=123, cinfoDao={arg=123, id=123}}, id=123}}
//        {name=123, cinfoDao={arg={name=123, cinfoDao={arg=123, id=123}}, id=123}}
//        Student{stuId='123123', isStudent=true, humans=[Human{name='zs', age=32}], newStudents=[1, 2, 3], name='haha', age=12}
//        Student{stuId='123123', isStudent=true, humans=[Human{name='zs', age=32}], newStudents=[1, 2, 3], name='haha', age=12}
//        Student{stuId='123123', isStudent=true, humans=[Human{name='zs', age=32}], newStudents=[1, 2, 3], name='haha', age=12}
//        {"stuId":"123123","isStudent":true,"humans":   [{"name":"zs","age":32  }],   " newStudents":[1,2,3]   ,  "name":"haha","age":12}
    }
}
