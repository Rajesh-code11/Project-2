import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentManagementSystem extends JFrame {
    private JTextField idField, nameField, ageField, classField, searchField;
    private JButton addButton, deleteButton, searchButton, resetButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private ArrayList<Student> students;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        students = new ArrayList<>();
        String[] columnNames = {"ID", "Name", "Age", "Class"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Class:"));
        classField = new JTextField();
        inputPanel.add(classField);

        inputPanel.add(new JLabel("Search:"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Student");
        deleteButton = new JButton("Delete Student");
        searchButton = new JButton("Search");
        resetButton = new JButton("Reset");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        addButton.addActionListener(new AddButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        searchButton.addActionListener(new SearchButtonListener());
        resetButton.addActionListener(new ResetButtonListener());

        addSampleData();
        updateTable();

        setVisible(true);
    }

    private void addSampleData() {
        students.add(new Student("N221833001", "Zhang San", 20, "23 CS Class 1"));
        students.add(new Student("N221833002", "Li Si", 21, "23 CS Class 1"));
        students.add(new Student("N221833003", "Wang Wu", 20, "23 CS Class 2"));
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAge(), s.getClassName()});
        }
    }

    private class Student {
        private String id;
        private String name;
        private int age;
        private String className;

        public Student(String id, String name, int age, String className) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.className = className;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getClassName() { return className; }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String className = classField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
                JOptionPane.showMessageDialog(StudentManagementSystem.this,
                        "ID, name, and age cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);
                if (age <= 0) {
                    JOptionPane.showMessageDialog(StudentManagementSystem.this,
                            "Age must be a positive integer", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (Student s : students) {
                    if (s.getId().equals(id)) {
                        JOptionPane.showMessageDialog(StudentManagementSystem.this,
                                "ID already exists", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                students.add(new Student(id, name, age, className));
                updateTable();
                clearInputFields();
                JOptionPane.showMessageDialog(StudentManagementSystem.this,
                        "Student added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudentManagementSystem.this,
                        "Age must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(StudentManagementSystem.this,
                        "Please select a student to delete", "Operation Prompt", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(StudentManagementSystem.this,
                    "Are you sure to delete the selected student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                students.remove(selectedRow);
                updateTable();
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                updateTable();
                return;
            }

            ArrayList<Student> searchResults = new ArrayList<>();
            for (Student s : students) {
                if (s.getId().contains(keyword) || s.getName().contains(keyword)) {
                    searchResults.add(s);
                }
            }

            tableModel.setRowCount(0);
            for (Student s : searchResults) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAge(), s.getClassName()});
            }

            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(StudentManagementSystem.this,
                        "No matching students found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(StudentManagementSystem.this,
                    "Are you sure to reset all student data?", "Confirm Reset", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                students.clear();
                updateTable();
                clearInputFields();
                searchField.setText("");
            }
        }
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        classField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystem());
    }
}