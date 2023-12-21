package com.txl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StudentSystem extends JFrame implements ActionListener {//继承自JFrame使得这个类成为一个窗体,可以对窗体的属性进行扩展并且可以定义自己需要的特殊操作方法
    private static final long serialVersionUID = 1L;//把java对象序列化而后进行保存
    private Map<String, String> PersonInfo;
    public static Vector<Vector<String>> info = new Vector<Vector<String>>();
    private JLabel keyLab;
    private JButton searchBtn, createBtn, updateBtn, deleteBtn,exitBtn,refreshBtn,startBtn,stopBtn;
    public static JTable infoTable;
    private JTextField keyText;
    public static Vector<String> column;
//    String[] options = {"编号", "用户名", "密码", "级别", "联系邮箱"};
    String[] options = {"id", "name", "password", "level", "email"};
    JComboBox<String> search = new JComboBox<String>(options);

    public StudentSystem() {
        PersonInfo = new HashMap<String, String>();//数组和链表的结合体,HashMap底层就是一个数组结构，数组中的每一项又是一个链表。新建一个HashMap的时候，就会初始化一个数组
        Font font = new Font("宋体", Font.PLAIN, 15);//设置字体，类型和大小；Front.PLAIN普通，Front.BLOD加粗，Front.ITALIC斜体
        JPanel pNorth = new JPanel();
        pNorth.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel bEast = new JPanel();
        bEast.setLayout(new GridLayout(0,1));


        JLabel text = new JLabel("请选择查询条件");
        keyLab = new JLabel();
        keyText = new JTextField(10);//搜索文本框

        //创建系统功能按钮
        searchBtn = new JButton("查询");
        createBtn = new JButton("添加");
        updateBtn = new JButton("修改");
        deleteBtn = new JButton("删除");
        exitBtn = new JButton("退出");
        refreshBtn = new JButton("刷新");
        startBtn = new JButton("开始查询");
        stopBtn = new JButton("结束查询");


        //设置字体大小
        keyLab.setFont(font);
        searchBtn.setFont(font);
        createBtn.setFont(font);
        updateBtn.setFont(font);
        deleteBtn.setFont(font);
        exitBtn.setFont(font);
        refreshBtn.setFont(font);
        startBtn.setFont(font);
        stopBtn.setFont(font);

        //添加监听器
        searchBtn.addActionListener(this);
        createBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        refreshBtn.addActionListener(this);
        startBtn.addActionListener(this);
        stopBtn.addActionListener(this);

        //在JPanel面板的上方加入搜索功能所需的一系列组件
        pNorth.add(keyLab);
        pNorth.add(keyText);

        //在JPanel面板下方加入系统功能组件
        bEast.add(createBtn);
        bEast.add(updateBtn);
        bEast.add(deleteBtn);
        bEast.add(searchBtn);
        bEast.add(refreshBtn);
        bEast.add(exitBtn);
        bEast.add(text);
        bEast.add(search);
        bEast.add(keyText);
        bEast.add(startBtn);
        bEast.add(stopBtn);
        this.add(bEast, BorderLayout.EAST);


        refreshBtn.addActionListener(e -> {
            // 点击刷新按钮时，重新执行查询并刷新表格数据
            searchInfo((String) search.getSelectedItem(), keyText.getText());
        });
        //表格数据
        column = new Vector<String>();
        column.add("编号");
        column.add("用户名");
        column.add("密码");
        column.add("级别");
        column.add("联系邮箱");
        flashInfo();//将数据存入数据库
        infoTable = new JTable(info, column);
        TableColumn column1 = infoTable.getColumnModel().getColumn(0);
        column1.setPreferredWidth(30);//自适应

        TableColumn column3 = infoTable.getColumnModel().getColumn(2);
        column3.setPreferredWidth(30);//自适应

        JScrollPane pCenter = new JScrollPane(infoTable);//创建垂直滚动面板
        this.add(pNorth, "North");
        this.add(pCenter, "Center");
        this.add(bEast, "South");

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pCenter, bEast);
        splitPane.setDividerLocation(400);
        this.add(splitPane);

        this.setTitle("用户信息管理系统");
        this.setSize(800, 450);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }




    public static void flashInfo() {
        Connect dbconn = new Connect();
        Statement stmt = null;
        ResultSet rs = null;
        info.clear();
        try {
            stmt = dbconn.getConnection().createStatement();//创建一个 Statement 对象，封装 SQL 语句发送给数据库
            rs = stmt.executeQuery("select * from student_one");//下达命令执行查询语句并且存放在ResultSet对象中
            while (rs.next()) {
                Vector<String> row = new Vector<String>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                row.add(rs.getString(5));
                info.add(row);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();//在命令行打印异常信息在程序中出错的位置及原因
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        JPanel j1 = new JPanel();
        int rowNum = infoTable.getSelectedRow();//返回第一个选定行的索引
        if (rowNum != -1) {
            PersonInfo = new HashMap<String, String>();
            //将值插入HasMap中
            PersonInfo.put("id", (String) infoTable.getValueAt(rowNum, 0));//返回表格row和column位置的单元格值
            PersonInfo.put("name", (String) infoTable.getValueAt(rowNum, 1));
            PersonInfo.put("password", (String) infoTable.getValueAt(rowNum, 2));
            PersonInfo.put("level", (String) infoTable.getValueAt(rowNum, 3));
            PersonInfo.put("email", (String) infoTable.getValueAt(rowNum, 4));
        }

        if (e.getSource() == searchBtn || e.getSource() == startBtn) {//搜索
            String keyStr = keyText.getText();
            String selectedOption = (String)search.getSelectedItem();
            searchInfo(selectedOption,keyStr);
        } else if (e.getSource() == createBtn) {//新建
            MyDialog InsertPane = new MyDialog("新建用户信息", new HashMap<String, String>());
            InsertPane.setVisible(true);
        } else if (e.getSource() == updateBtn) {//修改
            if (rowNum == -1) {
                JOptionPane.showMessageDialog(null, "请选择用户");//提示弹窗
            }
            MyDialog UpdatePane = new MyDialog("修改用户信息", PersonInfo);
            UpdatePane.setVisible(true);
        } else if (e.getSource() == deleteBtn) {//删除
            if (rowNum == -1) {
                JOptionPane.showMessageDialog(null, "请选择用户");//提示弹窗
            }
            MyDialog DeletePane = new MyDialog("删除用户信息", PersonInfo);
            DeletePane.setVisible(true);
        } else if(e.getSource()==exitBtn) {//退出
            this.setVisible(false);
        } else if (e.getSource()==stopBtn) {
            int result = JOptionPane.showConfirmDialog(this, "是否保存并退出查询？","确认",JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_NO_OPTION){
                this.setVisible(false);
            }
        } else if (e.getSource()==refreshBtn) {//刷新
            searchInfo((String) search.getSelectedItem(), keyText.getText());
        }
    }
    private void handleSearch(){
        String keyStr = keyText.getText();
        String selectedOption = (String)search.getSelectedItem();
        searchInfo(selectedOption,keyStr);
    }

    protected void searchInfo(String selectedOption,String key) {//搜索
            Connect dbconn = new Connect();
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;

            try {
                String sql = "SELECT * FROM student_one WHERE " + selectedOption + " LIKE ?";
                preparedStatement = dbconn.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, "%" + key + "%");

                rs = preparedStatement.executeQuery();
                info.clear();

                while (rs.next()) {
                    Vector<String> row = new Vector<>();
                    row.add(rs.getString(1));
                    row.add(rs.getString(2));
                    row.add(rs.getString(3));
                    row.add(rs.getString(4));
                    row.add(rs.getString(5));
                    info.add(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

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
    public static void main(String[] args) {
        new StudentSystem();
    }
}


