import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;
/*
 * Created by JFormDesigner on Sat Aug 20 15:33:14 ICT 2022
 */

/**
 * @author Minh Huy
 */

public class StudentManagement extends JFrame {

    ArrayList<Student> studentArrayList = new ArrayList<>();
    static String dbFile = "StudentManagement.txt";

    public static void main(String[] args) throws IOException {
        StudentManagement student = new StudentManagement();
        student.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        student.setVisible(true);
    }

    public StudentManagement() throws IOException {
        initComponents();
        returnTable(readFile());
        List<String> list = Files.readAllLines(new File(dbFile).toPath(), Charset.defaultCharset());
        for (String line : list) {
            String[] res = line.split(",");
            Student user = new Student(res[0], res[1], res[2], res[3]);
            studentArrayList.add(user);
        }
    }

    // Clear text inside the text field when implement function
    private void Clear() {
        idTextField.setText("");
        nameTextField.setText("");
        gradeTextField.setText("");
    }


    // ADD STUDENT
    private void addBtn(ActionEvent e) {
        // TODO add your code here
        String name = nameTextField.getText().trim();
        String id = idTextField.getText().trim();
        String gender;
        if (maleRadioButton.isSelected()){
            gender = maleRadioButton.getText();
        }else if (femaleRadioButton.isSelected()){
            gender = femaleRadioButton.getText();
        }else if (otherRadioButton.isSelected()){
            gender = otherRadioButton.getText();
        }else {
            gender = "";
        }
        String grade = gradeTextField.getText().toUpperCase();

        if (!id.isEmpty() && !name.isEmpty()) {
            if (CheckId(id)) {
                JOptionPane.showMessageDialog(null, "ID is duplicated", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!CheckIdValid(id)) {
                JOptionPane.showMessageDialog(null, "Invalid ID! Try Again", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (!CheckNameValid(name)) {
                JOptionPane.showMessageDialog(null, "Name is invalid", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!CheckGradeValid(grade)){
                JOptionPane.showMessageDialog(null, "Grade field can not be blank and should be P,M or D"
                        , "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(id, name, gender, grade);
            studentArrayList.add(student);
            writeFile(studentArrayList);
            Clear();
            clearTable();
            returnTable(readFile());
        }
        else {
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill ID Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill Name Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    // DELETE STUDENT
    private void deleteButton(ActionEvent e) {
        // TODO add your code here
        String id = idTextField.getText().trim();
        for (int i = 0; i < studentArrayList.size(); i++) {
            if (studentArrayList.get(i).getId().equals(id)) {
                studentArrayList.remove(i);
            }
        }
        writeFile(studentArrayList);
        Clear();
        clearTable();
        returnTable(readFile());
    }

    //UPDATE STUDENT
    private void updateBtn() {
        // TODO add your code here
        String id = idTextField.getText().trim();
        String name = nameTextField.getText().trim();
        String gender;
        if (maleRadioButton.isSelected()){
            gender = maleRadioButton.getText();
        }else if (femaleRadioButton.isSelected()){
            gender = femaleRadioButton.getText();
        }else if (otherRadioButton.isSelected()){
            gender = otherRadioButton.getText();
        }else {
            gender = "";
        }
        String grade = gradeTextField.getText().toUpperCase();
        if (!id.isEmpty() && !name.isEmpty()) {
            if (!CheckNameValid(name)) {
                JOptionPane.showMessageDialog(null, "Name is invalid", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Student student : studentArrayList) {
                if (student.getId().equals(id)) {
                    student.setName(name);
                    student.setGender(gender);
                    student.setGrade(grade);
                }
            }
            writeFile(studentArrayList);
            Clear();
            clearTable();
            returnTable(readFile());
        } else {
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill ID Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill Name Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }


    // SEARCH STUDENT
    private void searchBtn() {
        // TODO add your code here
        String id = idTextField.getText().trim();
        clearTable();
        if (!id.isEmpty()){
            for (Student student : studentArrayList) {
                if (student.getId().equals(id)) {
                    Student findedStudent = new Student(student.getId(), student.getName(), student.getGender(), student.getGrade());
                    clearTable();
                    returnFindedStudentsToTable(findedStudent);
                }
            }
        } else{
            returnTable(readFile());
            JOptionPane.showMessageDialog(null, "Filling Search Field Required", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }


    // Return user by Object
    public void returnFindedStudentsToTable(Student student){
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableStudent.getModel();
        String[] findedUser = student.toString().split(",");
        defaultTableModel.addRow(findedUser);
    }

    // Clear all the contents of table
    public void clearTable(){
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableStudent.getModel();
        defaultTableModel.setRowCount(0);
    }

    // Read file
    public Object[] readFile(){
        Object[] objects;
        try {
            FileReader fr = new FileReader(dbFile);
            BufferedReader bufferedReader = new BufferedReader(fr);
            // each lines to array
            objects = bufferedReader.lines().toArray();
            bufferedReader.close();
            return objects;
        } catch (IOException e) {}
        return null;
    }

    // Write from list to file
    public static void writeFile(ArrayList<Student> _users){
        try{
            FileWriter fw = new FileWriter(dbFile);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Student user: _users
            ) {
                bw.write(user.toString());
                bw.newLine();
            }
            bw.close();
            fw.close();

        }catch (Exception exception){}
    }

    // Return user by Object array
    public void returnTable(Object[] objects){
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableStudent.getModel();
        int i = 0;
        while(i < objects.length) {
            String row = objects[i].toString().trim();
            String[] rows = row.split(",");
            defaultTableModel.addRow(rows);
            i++;
        }
    }

    // Check Information of Student
    // Check ID
    public boolean CheckId(String id){
        for (Student user : studentArrayList) {
            if (user.getId().equals(id)) {
                return true;
            }
        }return false;
    }

    // Check name valid
    public boolean CheckNameValid(String name){
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);

            // If the character is not a letter and not a whitespace, return false.
            if (!Character.isLetter(ch) && !Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }

    // Check ID valid
    public boolean CheckIdValid(String id){
            try
            {
                Integer.parseInt(id);
                return true;
            }

            catch (NumberFormatException e){return false;}
    }

    // Check grade valid
    public boolean CheckGradeValid(String grade){
        if (grade.equalsIgnoreCase("P")
                || grade.equalsIgnoreCase("M")
                || grade.equalsIgnoreCase("D")){
            return true;
        }
        return false;
    }

    private void otherRadioButton(ActionEvent e) {
        // TODO add your code here
    }

    private void femaleRadioButton(ActionEvent e) {
        // TODO add your code here
    }

    private void maleRadioButton(ActionEvent e) {
        // TODO add your code here
    }

    private void male(ActionEvent e) {
        // TODO add your code here
    }

    private void sortBtn() {
        // TODO add your code here
    }

    private void search() {
        // TODO add your code here
    }
    private void searchBtn(ActionEvent e) {

    }

    private void update(ActionEvent e) {
        // TODO add your code here

    }

    private void deleteBtn(ActionEvent event) {
        // TODO add your code here
    }

    private void delete(ActionEvent e) {
        // TODO add your code here
    }

    private void deleteBtn() {
        // TODO add your code here
    }

    private void deleteFunction() {
        // TODO add your code here
    }

    private void add(ActionEvent e) {
        // TODO add your code here
    }

    private void addBtn() {
        // TODO add your code here
    }

    private void add() {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Minh Huy
        panel1 = new JPanel();
        nameLabel = new JLabel();
        genderLabel = new JLabel();
        gradeLabel = new JLabel();
        idLabel = new JLabel();
        nameTextField = new JTextField();
        idTextField = new JTextField();
        otherRadioButton = new JRadioButton();
        femaleRadioButton = new JRadioButton();
        maleRadioButton = new JRadioButton();
        gradeTextField = new JTextField();
        wrongInput = new JLabel();
        scrollPane1 = new JScrollPane();
        tableStudent = new JTable();
        addButton = new JButton();
        searchButton = new JButton();
        updateButton = new JButton();
        deleteButton = new JButton();

        //======== this ========
        setTitle("Student Management");
        var contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
            , 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
            , new java .awt .Font ("Dialo\u0067" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
            panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
            ) {if ("borde\u0072" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

            //---- nameLabel ----
            nameLabel.setText("Full Name");

            //---- genderLabel ----
            genderLabel.setText("Gender");

            //---- gradeLabel ----
            gradeLabel.setText("Grade");

            //---- idLabel ----
            idLabel.setText("ID");

            //---- otherRadioButton ----
            otherRadioButton.setText("Other");
            otherRadioButton.addActionListener(e -> otherRadioButton(e));

            //---- femaleRadioButton ----
            femaleRadioButton.setText("Female");
            femaleRadioButton.addActionListener(e -> femaleRadioButton(e));

            //---- maleRadioButton ----
            maleRadioButton.setText("Male");
            maleRadioButton.addActionListener(e -> {
			maleRadioButton(e);
			male(e);
		});

            //======== scrollPane1 ========
            {

                //---- tableStudent ----
                tableStudent.setModel(new DefaultTableModel(
                    new Object[][] {
                    },
                    new String[] {
                        "ID", "Full Name", "Gender", "Grade"
                    }
                ) {
                    boolean[] columnEditable = new boolean[] {
                        false, false, false, false
                    };
                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return columnEditable[columnIndex];
                    }
                });
                {
                    TableColumnModel cm = tableStudent.getColumnModel();
                    cm.getColumn(0).setPreferredWidth(50);
                    cm.getColumn(1).setPreferredWidth(150);
                    cm.getColumn(2).setPreferredWidth(50);
                    cm.getColumn(3).setPreferredWidth(50);
                }
                scrollPane1.setViewportView(tableStudent);
            }

            //---- addButton ----
            addButton.setText("Add");
            addButton.addActionListener(e -> {
			add(e);
			addBtn(e);
			addBtn();
			addBtn();
			add();
		});

            //---- searchButton ----
            searchButton.setText("Search");
            searchButton.addActionListener(e -> {
			searchBtn();
			sortBtn();
			sortBtn();
			search();
		});

            //---- updateButton ----
            updateButton.setText("Update");
            updateButton.addActionListener(e -> {
			update(e);
			updateBtn();
		});

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.addActionListener(e -> {
			delete(e);
			deleteButton(e);
			deleteBtn();
			deleteFunction();
		});

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(idLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36)
                                                .addComponent(idTextField, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(genderLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(maleRadioButton, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(femaleRadioButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(otherRadioButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
                                        .addGap(34, 34, 34)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(addButton, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                            .addComponent(updateButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(deleteButton, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                            .addComponent(searchButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGap(355, 355, 355)
                                        .addComponent(wrongInput, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(gradeLabel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(gradeTextField, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 70, Short.MAX_VALUE))
                            .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                                .addContainerGap())))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(idLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(addButton)
                            .addComponent(searchButton)
                            .addComponent(idTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(maleRadioButton)
                            .addComponent(femaleRadioButton)
                            .addComponent(otherRadioButton)
                            .addComponent(genderLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateButton)
                            .addComponent(deleteButton))
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(wrongInput))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(gradeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gradeLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 72, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(otherRadioButton);
        buttonGroup1.add(femaleRadioButton);
        buttonGroup1.add(maleRadioButton);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }



    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Minh Huy
    private JPanel panel1;
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel gradeLabel;
    private JLabel idLabel;
    private JTextField nameTextField;
    private JTextField idTextField;
    private JRadioButton otherRadioButton;
    private JRadioButton femaleRadioButton;
    private JRadioButton maleRadioButton;
    private JTextField gradeTextField;
    private JLabel wrongInput;
    private JScrollPane scrollPane1;
    private JTable tableStudent;
    private JButton addButton;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
