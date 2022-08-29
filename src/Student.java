public class Student {
    public String id, name, gender, grade;

    public Student() { }
    public Student(String id, String name, String gender, String grade) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.grade = grade;
    }
    public String getId() {return id;}

    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public String getGrade() {return grade;}
    public void setGrade(String grade) {this.grade = grade;}
    @Override
    public String toString() {return id + ","+ name+ "," + gender+ "," + grade;}
}
