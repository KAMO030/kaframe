package json_test.pojo;

public class Human {
    protected  String name;
    protected Integer age;

    public String getName() {
        return name;
    }

    public Human setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Human setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
