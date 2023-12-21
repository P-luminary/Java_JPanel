package com.txl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//登录界面
public class Login {
    public static void main(String args[]) {
        Login l=new Login();//实例化Login对象
        l.showUI();
    }

    public void showUI() {
        JFrame login=new JFrame();//创建一个JFrame容器窗口
        login.setTitle("登录系统");//设置标题
        login.setSize(340,240);//设置窗口大小
        login.setDefaultCloseOperation(3);//0-DO_NITHING窗口无法关闭;1-HIDE隐藏程序界面但没有关闭程序;2-DISPOSE自动隐藏释放窗体，继续运行应用程序;3-EXIT
        login.setLocationRelativeTo(null);//设置窗口位置相对于指定组件的位置
        login.setResizable(false);//设置窗口不可被调整大小，布尔值
        //FlowLayout fl=new FlowLayout(FlowLayout.CENTER,5,5);
        login.setLayout(new FlowLayout());//FloeLayout默认居中对齐，水平、垂直间距默认为5个单位
        login.setVisible(true);//窗体可见

        //用户名标签组件
        JLabel labname=new JLabel();
        labname.setText("用户名：");
        labname.setPreferredSize(new Dimension(60, 60));//设置最适合窗口的位置(setPreferredSize)和JLable标签组件的宽度和高度(Dimension)
        login.add(labname);//加入JFrame窗口
        JTextField textname=new JTextField();//创建一个JTextField文本框用于输入用户名
        textname.setPreferredSize(new Dimension(250, 30));
        login.add(textname);//加入到JFrame窗口中
        //密码标签组件
        JLabel labpassword=new JLabel();
        labpassword.setText("密    码：");
        labpassword.setPreferredSize(new Dimension(60, 60));
        login.add(labpassword);
        JPasswordField jp=new JPasswordField();
        jp.setPreferredSize(new Dimension(250, 30));
        login.add(jp);

        //登录按钮
        JButton button=new JButton();
        button.setText("登录");
        button.setPreferredSize(new Dimension(100, 40));
        login.add(button);
        login.setVisible(true);

        //为登录键添加鼠标事件监听器
        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Connect dbconn = new Connect();//实例化Connect对象
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    //用于创建一个 Statement 对象，封装 SQL 语句发送给数据库，通常用来执行不带参数的 SQL 语句
                    stmt = dbconn.getConnection().createStatement();
                    //执行查询；用statement类的executeQuery()方法来下达select指令以查询数据库，把数据库响应的查询结果存放在ResultSet类对象中供我们使用
                    //select * from查询在数据库中表内信息
                    rs = stmt.executeQuery("select * from login where username='"+textname.getText()+"' and password='"+jp.getText()+"'");
                    if (rs.next()) {
                        new StudentSystem();//主界面
                        login.dispose();//释放登录界面窗口占用的屏幕资源
                    }else{
                        JOptionPane.showMessageDialog(null, "用户名或密码不正确!!!","提示",2);//java弹窗JOptionPane.showMessageDialog(null, "提示内容" ,"标题", -1~3);
                    }
                    rs.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();//在命令行打印异常信息在程序中出错的位置及原因,显示出更深的调用信息
                    //System.out.println(e1);
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}