package json_test.pojo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student extends Human {
    private String stuId;
    private Boolean isStudent;

    private List<Human> humans;

    private String[] newStudents;
    public String getStuId() {
        return stuId;
    }

    public Student setStuId(String stuId) {
        this.stuId = stuId;
        return this;
    }

    public Boolean getStudent() {
        return isStudent;
    }

    public Student setStudent(Boolean student) {
        isStudent = student;
        return this;
    }

    public List<Human> getHumans() {
        return humans;
    }

    public Student setHumans(List<Human> humans) {
        this.humans = humans;
        return this;
    }

    public String[] getNewStudents() {
        return newStudents;
    }

    public Student setNewStudents(String[] newStudents) {
        this.newStudents = newStudents;
        return this;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId='" + stuId + '\'' +
                ", isStudent=" + isStudent +
                ", humans=" + humans +
                ", newStudents=" + Arrays.toString(newStudents) +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
