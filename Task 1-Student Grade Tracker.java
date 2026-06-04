import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

// OOP concepts used: encapsulation, inheritance, interface, abstraction

// Interface - defines what a gradable object must do
interface Gradable {
    void addGrade(double grade);
    double getAverage();
    double getHighest();
    double getLowest();
    String getLetterGrade();
}

// Abstract class - base for any person in the system
abstract class Person {
    private String name;
    private int id;
    private static int nextId = 1001;

    public Person(String name) {
        this.name = name;
        this.id = nextId++;
    }

    public String getName() { return name; }
    public int getId()      { return id; }

    public abstract String getSummary();
}

// Student extends Person and implements Gradable
class Student extends Person implements Gradable {

    private ArrayList<Double> grades = new ArrayList<>();

    public Student(String name) {
        super(name);
    }

    @Override
    public void addGrade(double grade) {
        if (grade < 0 || grade > 100)
            throw new IllegalArgumentException("Grade must be 0–100.");
        grades.add(grade);
    }

    @Override
    public double getAverage() {
        if (grades.isEmpty()) return 0;
        double total = 0;
        for (double g : grades) total += g;
        return total / grades.size();
    }

    @Override
    public double getHighest() {
        if (grades.isEmpty()) return 0;
        double max = grades.get(0);
        for (double g : grades) if (g > max) max = g;
        return max;
    }

    @Override
    public double getLowest() {
        if (grades.isEmpty()) return 0;
        double min = grades.get(0);
        for (double g : grades) if (g < min) min = g;
        return min;
    }

    @Override
    public String getLetterGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A";
        if (avg >= 80) return "B";
        if (avg >= 70) return "C";
        if (avg >= 60) return "D";
        return "F";
    }

    @Override
    public String getSummary() {
        if (grades.isEmpty()) return getName() + " — no grades yet";
        return String.format("%s | Avg: %.1f | %s", getName(), getAverage(), getLetterGrade());
    }

    public boolean hasGrades()            { return !grades.isEmpty(); }
    public ArrayList<Double> getGrades()  { return grades; }
    public int getGradeCount()            { return grades.size(); }
}


// ─── LOGIN DIALOG ────────────────────────────────────────────────────────────

class LoginDialog extends JDialog {

    private static final String USER = "faculty";
    private static final String PASS = "1234";

    boolean authenticated = false;

    private JTextField userField  = new JTextField(18);
    private JPasswordField passField = new JPasswordField(18);
    private JLabel errorLabel = new JLabel(" ");

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);
        setSize(360, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 246, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Faculty Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(40, 50, 80));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Use: faculty / 1234");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(Color.GRAY);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBackground(new Color(70, 100, 220));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> attemptLogin());

        // Allow Enter key on password field
        passField.addActionListener(e -> attemptLogin());

        panel.add(title);
        panel.add(Box.createVerticalStrut(4));
        panel.add(hint);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JLabel("Username:"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(userField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Password:"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(loginBtn);

        setContentPane(panel);
    }

    private void attemptLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        if (u.equals(USER) && p.equals(PASS)) {
            authenticated = true;
            dispose();
        } else {
            errorLabel.setText("Wrong username or password.");
            passField.setText("");
        }
    }
}


// ─── MAIN APPLICATION ────────────────────────────────────────────────────────

public class Task1 extends JFrame {

    // colors
    private static final Color BG      = new Color(245, 246, 250);
    private static final Color WHITE   = Color.WHITE;
    private static final Color BLUE    = new Color(70, 100, 220);
    private static final Color GREEN   = new Color(45, 160, 100);
    private static final Color RED     = new Color(210, 70, 70);
    private static final Color NAVY    = new Color(30, 40, 70);
    private static final Color GRAY    = new Color(120, 125, 145);

    // fonts
    private static final Font BOLD   = new Font("SansSerif", Font.BOLD, 13);
    private static final Font PLAIN  = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font SMALL  = new Font("SansSerif", Font.PLAIN, 11);

    // data
    private ArrayList<Student> students = new ArrayList<>();

    // UI elements we need to update
    private JComboBox<String> studentPicker = new JComboBox<>();
    private DefaultTableModel tableModel;
    private JLabel lblStudents, lblAvg, lblHigh, lblLow;
    private JLabel statusLabel;

    public Task1() {
        setTitle("Student Grade Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 620);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(makeHeader(),  BorderLayout.NORTH);
        root.add(makeMain(),    BorderLayout.CENTER);
        root.add(makeStatus(),  BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── TOP HEADER BAR ────────────────────────────────────────────────────────

    private JPanel makeHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("📊  Student Grade Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(SMALL);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 255, 255, 30));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Log out?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) { dispose(); showLogin(); }
        });

        header.add(title,     BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    // ── MAIN CONTENT (left + right) ───────────────────────────────────────────

    private JSplitPane makeMain() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, makeLeftPanel(), makeRightPanel());
        split.setDividerLocation(320);
        split.setDividerSize(2);
        split.setBorder(null);
        return split;
    }

    // ── LEFT PANEL ────────────────────────────────────────────────────────────

    private JPanel makeLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 10));

        panel.add(makeAddStudentCard());
        panel.add(Box.createVerticalStrut(12));
        panel.add(makeAddGradeCard());
        panel.add(Box.createVerticalStrut(12));
        panel.add(makeStatsCard());

        return panel;
    }

    private JPanel makeAddStudentCard() {
        JPanel card = makeCard("Add Student");

        JTextField nameField = new JTextField();
        nameField.setFont(PLAIN);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton addBtn = makeButton("Add Student", BLUE);
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { showError("Enter a name."); return; }

            for (Student s : students) {
                if (s.getName().equalsIgnoreCase(name)) {
                    showError("Student already exists."); return;
                }
            }

            students.add(new Student(name));
            nameField.setText("");
            refreshUI();
            setStatus("Added student: " + name);
        });

        card.add(new JLabel("Student Name:"));
        card.add(Box.createVerticalStrut(4));
        card.add(nameField);
        card.add(Box.createVerticalStrut(10));
        card.add(addBtn);
        return card;
    }

    private JPanel makeAddGradeCard() {
        JPanel card = makeCard("Add Grade");

        studentPicker.setFont(PLAIN);
        studentPicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JTextField gradeField = new JTextField();
        gradeField.setFont(PLAIN);
        gradeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton addBtn = makeButton("Add Grade", GREEN);
        addBtn.addActionListener(e -> {
            if (studentPicker.getItemCount() == 0) { showError("Add a student first."); return; }

            double grade;
            try {
                grade = Double.parseDouble(gradeField.getText().trim());
            } catch (NumberFormatException ex) {
                showError("Enter a valid number."); return;
            }

            String selectedName = (String) studentPicker.getSelectedItem();
            for (Student s : students) {
                if (s.getName().equals(selectedName)) {
                    try {
                        s.addGrade(grade);
                    } catch (IllegalArgumentException ex) {
                        showError(ex.getMessage()); return;
                    }
                    break;
                }
            }

            gradeField.setText("");
            refreshUI();
            setStatus(String.format("Added %.1f for %s", grade, selectedName));
        });

        card.add(new JLabel("Student:"));
        card.add(Box.createVerticalStrut(4));
        card.add(studentPicker);
        card.add(Box.createVerticalStrut(8));
        card.add(new JLabel("Score (0–100):"));
        card.add(Box.createVerticalStrut(4));
        card.add(gradeField);
        card.add(Box.createVerticalStrut(10));
        card.add(addBtn);
        return card;
    }

    private JPanel makeStatsCard() {
        JPanel card = makeCard("Class Overview");
        card.setLayout(new GridLayout(2, 2, 8, 8));

        lblStudents = addStatTile(card, "Students", "0",  BLUE);
        lblAvg      = addStatTile(card, "Average",  "—",  new Color(100, 70, 200));
        lblHigh     = addStatTile(card, "Highest",  "—",  GREEN);
        lblLow      = addStatTile(card, "Lowest",   "—",  RED);

        return card;
    }

    private JLabel addStatTile(JPanel parent, String title, String value, Color color) {
        JPanel tile = new JPanel();
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBackground(WHITE);
        tile.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 222, 235)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SMALL);
        titleLabel.setForeground(GRAY);

        tile.add(valueLabel);
        tile.add(titleLabel);
        parent.add(tile);
        return valueLabel;
    }

    // ── RIGHT PANEL ───────────────────────────────────────────────────────────

    private JPanel makeRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 10, 16, 16));

        panel.add(makeTablePanel(),   BorderLayout.CENTER);
        panel.add(makeBottomButtons(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel makeTablePanel() {
        // Table columns
        String[] columns = {"ID", "Name", "Grades", "Average", "Highest", "Lowest", "Letter"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(PLAIN);
        table.setRowHeight(34);
        table.setBackground(WHITE);
        table.setGridColor(new Color(230, 232, 240));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(70, 100, 220, 25));

        // Style the header
        JTableHeader header = table.getTableHeader();
        header.setFont(BOLD);
        header.setBackground(new Color(240, 242, 252));
        header.setForeground(NAVY);

        // Color the letter grade column
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(BOLD);
                String letter = val == null ? "—" : val.toString();
                switch (letter) {
                    case "A" -> lbl.setForeground(GREEN);
                    case "B" -> lbl.setForeground(BLUE);
                    case "C" -> lbl.setForeground(new Color(180, 130, 0));
                    case "F" -> lbl.setForeground(RED);
                    default  -> lbl.setForeground(GRAY);
                }
                return lbl;
            }
        });

        // Set some column widths
        table.getColumnModel().getColumn(0).setMaxWidth(55);
        table.getColumnModel().getColumn(2).setMaxWidth(65);
        table.getColumnModel().getColumn(6).setMaxWidth(65);

        // Remove selected student button
        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.setFont(SMALL);
        removeBtn.setForeground(RED);
        removeBtn.setBorderPainted(false);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showError("Select a row first."); return; }

            String name = (String) tableModel.getValueAt(row, 1);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Remove " + name + " and all their grades?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                students.removeIf(s -> s.getName().equals(name));
                refreshUI();
                setStatus("Removed: " + name);
            }
        });

        // Top bar of the table panel
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 10));

        JLabel heading = new JLabel("All Students");
        heading.setFont(BOLD);
        heading.setForeground(NAVY);

        topBar.add(heading,   BorderLayout.WEST);
        topBar.add(removeBtn, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(WHITE);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WHITE);
        wrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 222, 235)));
        wrapper.add(topBar,     BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel makeBottomButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setBackground(BG);

        JButton clearBtn = new JButton("Clear All");
        clearBtn.setFont(SMALL);
        clearBtn.setForeground(GRAY);
        clearBtn.setBorderPainted(false);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            if (students.isEmpty()) return;
            int r = JOptionPane.showConfirmDialog(this, "Remove all students?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                students.clear(); refreshUI(); setStatus("Cleared all students.");
            }
        });

        JButton reportBtn = makeButton("Generate Report", new Color(90, 55, 190));
        reportBtn.addActionListener(e -> showReport());

        panel.add(clearBtn);
        panel.add(reportBtn);
        return panel;
    }

    // ── STATUS BAR ────────────────────────────────────────────────────────────

    private JPanel makeStatus() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(235, 237, 248));
        bar.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));

        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(SMALL);
        statusLabel.setForeground(GRAY);

        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
        Timer t = new Timer(3500, e -> statusLabel.setText("Ready."));
        t.setRepeats(false);
        t.start();
    }

    // ── REPORT ────────────────────────────────────────────────────────────────

    private void showReport() {
        if (students.isEmpty()) { showError("No students yet."); return; }

        StringBuilder sb = new StringBuilder();
        sb.append("STUDENT GRADE REPORT\n");
        sb.append("=".repeat(45)).append("\n\n");

        for (Student s : students) {
            sb.append("► ").append(s.getSummary()).append("\n");
            if (s.hasGrades()) {
                sb.append(String.format("  ID      : #%d\n",           s.getId()));
                sb.append(String.format("  Grades  : %s\n",           s.getGrades()));
                sb.append(String.format("  Average : %.2f (%s)\n",    s.getAverage(), s.getLetterGrade()));
                sb.append(String.format("  Highest : %.2f\n",         s.getHighest()));
                sb.append(String.format("  Lowest  : %.2f\n",         s.getLowest()));
            }
            sb.append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        area.setBackground(new Color(248, 249, 252));
        area.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 380));

        JOptionPane.showMessageDialog(this, scroll, "Report", JOptionPane.PLAIN_MESSAGE);
    }

    // ── REFRESH UI ────────────────────────────────────────────────────────────

    private void refreshUI() {
        // Update dropdown
        String prev = (String) studentPicker.getSelectedItem();
        studentPicker.removeAllItems();
        for (Student s : students) studentPicker.addItem(s.getName());
        if (prev != null) studentPicker.setSelectedItem(prev);

        // Update table
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                "#" + s.getId(),
                s.getName(),
                s.getGradeCount(),
                s.hasGrades() ? String.format("%.1f", s.getAverage()) : "—",
                s.hasGrades() ? String.format("%.1f", s.getHighest()) : "—",
                s.hasGrades() ? String.format("%.1f", s.getLowest())  : "—",
                s.hasGrades() ? s.getLetterGrade() : "—"
            });
        }

        // Update stat tiles
        lblStudents.setText(String.valueOf(students.size()));

        double totalAvg = 0, highestAll = 0, lowestAll = 100;
        int count = 0;

        for (Student s : students) {
            if (s.hasGrades()) {
                totalAvg   += s.getAverage();
                highestAll  = Math.max(highestAll, s.getHighest());
                lowestAll   = Math.min(lowestAll,  s.getLowest());
                count++;
            }
        }

        if (count > 0) {
            lblAvg.setText(String.format("%.1f", totalAvg / count));
            lblHigh.setText(String.format("%.1f", highestAll));
            lblLow.setText(String.format("%.1f", lowestAll));
        } else {
            lblAvg.setText("—");
            lblHigh.setText("—");
            lblLow.setText("—");
        }
    }

    // ── HELPER METHODS ────────────────────────────────────────────────────────

    // Creates a white rounded card panel with a bold title
    private JPanel makeCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 222, 235)),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel label = new JLabel(title);
        label.setFont(BOLD);
        label.setForeground(NAVY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(label);

        return card;
    }

    // Creates a styled blue/green/etc button
    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(BOLD);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    // ── LOGIN + ENTRY POINT ───────────────────────────────────────────────────

    static void showLogin() {
        LoginDialog login = new LoginDialog(null);
        login.setVisible(true);
        if (login.authenticated) {
            SwingUtilities.invokeLater(() -> new Task1().setVisible(true));
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // no problem, default look is fine
        }

        SwingUtilities.invokeLater(() -> showLogin());
    }
}