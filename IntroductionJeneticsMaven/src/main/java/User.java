import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class User {
    private String name;
    private String surname;
    int age;
    private List<String> skills;

   // private static int numOfUser=1;

    public User(String name, String surname, int age, List<String> skills) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.skills = skills;
    }

//    public User(String name, String surname, int age, int numOfskills) {
//        this.name = name;
//        this.surname = surname;
//        this.age = age;
//        this.skills = new ArrayList<>(Arrays.asList(String.valueOf(numOfskills)));
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    public List<String> setSkillsNum(int numOfSkills) {
       // this.skills =new ArrayList<>(Arrays.asList(String.valueOf(numOfSkills)));
        return this.skills;
    }

    @Override
    public String toString() {
        return "user"+/*numOfUser++ +*/"{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", skills=" + skills +
                '}';
    }
}
