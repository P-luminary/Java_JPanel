package com.txl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class MyDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;//把java对象序列化而后进行保存
    private Connect dbconn = new Connect();
    private static String id;
    private JPanel pCenter, pSouth;
    private JLabel idLab,nameLab,passwordLab,levelLab,emailLab;
    private JTextField idText, nameText, passwordText, levelText, emailText;
    private JComboBox<String> sex;
    private JButton yesBtn, noBtn;
    public MyDialog() {}

    public MyDialog(String title, Map<String, String> info) {
        id = info.get("id");
        if("删除联系人".equals(title)) {
            deletePerson();
        }else {
            Font font = new Font("宋体", Font.BOLD, 14);
            pCenter = new JPanel();
            pCenter.setLayout(new GridLayout(5, 1));
            idLab = new JLabel("编号：");
            nameLab = new JLabel("用户名：");
            passwordLab = new JLabel("密码：");
            levelLab = new JLabel("级别：");
            emailLab = new JLabel("联系邮箱：");

            idLab.setFont(font);
            nameLab.setFont(font);
            passwordLab.setFont(font);
            levelLab.setFont(font);
            emailLab.setFont(font);

            idText = new JTextField(10);
            nameText = new JTextField(10);
            passwordText = new JTextField(10);
            levelText = new JTextField(10);
            emailText = new JTextField(10);

            JPanel jp1 = new JPanel();
            jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp1.add(nameLab);
            jp1.add(nameText);
            nameText.setText(info.get("name"));

            JPanel jp2 = new JPanel();
            jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp2.add(passwordLab);
            jp2.add(passwordText);
            passwordText.setText(info.get("password"));

            JPanel jp3 = new JPanel();
            jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp3.add(levelLab);
            jp3.add(levelText);
            levelText.setText(info.get("level"));

            JPanel jp4 = new JPanel();
            jp4.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp4.add(emailLab);
            jp4.add(emailText);
            emailText.setText(info.get("email"));

            pCenter.add(jp1);
            pCenter.add(jp2);
            pCenter.add(jp3);
            pCenter.add(jp4);

            pSouth = new JPanel();
            yesBtn = new JButton("以数据库保存");
            yesBtn.addActionListener(this);
            noBtn = new JButton("以文件保存");
            noBtn.addActionListener(this);
            pSouth.add(yesBtn);
            pSouth.add(noBtn);

            this.add(pCenter, "Center");
            this.add(pSouth, "South");

            this.setTitle(title);
            this.setSize(400, 450);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    public void actionPerformed(ActionEvent e) {//系统功能按钮监听器
        if (e.getSource() == yesBtn) {
            if (this.getTitle().equals("新建用户信息")) {
                dbinsertPerson();
            } else if (this.getTitle().equals("修改用户信息")) {
                updatePerson();
            } else if (this.getTitle().equals("删除用户信息")) {
                deletePerson();
            }
        } else if (e.getSource() == noBtn) {
            fileinsertPerson();
        }
    }

    public void insertPerson() {
        if (nameText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入姓名！");//提示弹窗
            return;
        }
    }

    public void dbinsertPerson(){//将新建的用户信息写入数据库的表中
        boolean flag=true;
        String sql = "insert into student_one(name, password, level, email)value(?,?,?,?)";
        try {
            //PreparedStatement 对象已预编译过，所以其执行速度要快于 Statement 对象,多次执行的 SQL 语句经常创建为 PreparedStatement 对象，以提高效率。作为 Statement 的子类，PreparedStatement 继承了 Statement 的所有功能
            PreparedStatement pstmt = dbconn.getConnection().prepareStatement(sql);
            pstmt.setString(1, nameText.getText());
            pstmt.setString(2, passwordText.getText());
            pstmt.setString(3, levelText.getText());
            pstmt.setString(4, emailText.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "新建用户信息失败！");//提示弹窗
            flag = false;
        } finally {
            dispose();
            if (flag) {
                JOptionPane.showMessageDialog(null, "新建用户信息成功！");//提示弹窗
            }
            StudentSystem.flashInfo();//将数据写入数据库
            DefaultTableModel model = new DefaultTableModel(StudentSystem.info, StudentSystem.column);

            StudentSystem.infoTable.setModel(model);
            TableColumn column1 = StudentSystem.infoTable.getColumnModel().getColumn(0);
            column1.setMaxWidth(40);
            column1.setMinWidth(40);

            TableColumn column3 = StudentSystem.infoTable.getColumnModel().getColumn(2);
            column3.setMaxWidth(40);
            column3.setMinWidth(40);
        }
    }

    public void fileinsertPerson(){//将新建的用户信息写入文件中
        boolean flag=true;
        try {
            StringBuffer sbf=new StringBuffer();
                    sbf.append(nameText.getText()).append(" ")
                    .append(passwordText.getText()).append(" ")
                    .append(levelText.getText()).append(" ")
                    .append(emailText.getText());
            File file = new File("information.txt");
            FileOutputStream fos = null;
            if(!file.exists()){
                file.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(file);//首次写入获取
            }else{
                //如果文件已存在，就在文件末尾追加写入
                fos = new FileOutputStream(file,true);
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");//指定以UTF-8格式写入文件
            osw.write(sbf.toString());
            osw.write("\r\n");
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "新建用户信息失败！");//提示弹窗
            flag = false;
        } finally {
            dispose();
            if (flag) {
                JOptionPane.showMessageDialog(null, "新建用户信息成功！");//提示弹窗
            }
        }
    }

    public void deletePerson() {//删除信息
        String sql = "delete from student_one where id=?";

        try {
            //PreparedStatement 对象已预编译过，所以其执行速度要快于 Statement 对象,多次执行的 SQL 语句经常创建为 PreparedStatement 对象，以提高效率。作为 Statement 的子类，PreparedStatement 继承了 Statement 的所有功能
            PreparedStatement pstmt = dbconn.getConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dispose();
            StudentSystem.flashInfo();
            DefaultTableModel model = new DefaultTableModel(StudentSystem.info, StudentSystem.column);
            JOptionPane.showMessageDialog(null, "删除成功！");
            StudentSystem.infoTable.setModel(model);
            TableColumn column1 = StudentSystem.infoTable.getColumnModel().getColumn(0);
            column1.setMaxWidth(40);
            column1.setMinWidth(40);

            TableColumn column3 = StudentSystem.infoTable.getColumnModel().getColumn(2);
            column3.setMaxWidth(40);
            column3.setMinWidth(40);
        }
    }

    public void updatePerson() {//修改信息
        if (nameText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入姓名！");//提示弹窗
        }
        String sql = "update student_one set name=?,password=?,level=?,email=? where id=?";

        try {
            //PreparedStatement 对象已预编译过，所以其执行速度要快于 Statement 对象,多次执行的 SQL 语句经常创建为 PreparedStatement 对象，以提高效率。作为 Statement 的子类，PreparedStatement 继承了 Statement 的所有功能
            PreparedStatement pstmt = dbconn.getConnection().prepareStatement(sql);
            pstmt.setString(1, nameText.getText());
            pstmt.setString(2, passwordText.getText());
            pstmt.setString(3, levelText.getText());
            pstmt.setString(4, emailText.getText());
            pstmt.setString(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dispose();
            StudentSystem.flashInfo();
            DefaultTableModel model = new DefaultTableModel(StudentSystem.info, StudentSystem.column);

            StudentSystem.infoTable.setModel(model);
            TableColumn column1 = StudentSystem.infoTable.getColumnModel().getColumn(0);
            column1.setMaxWidth(40);
            column1.setMinWidth(40);

            TableColumn column3 = StudentSystem.infoTable.getColumnModel().getColumn(2);
            column3.setMaxWidth(40);
            column3.setMinWidth(40);
        }
    }
}