package fgoScript.entity;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import com.sun.awt.AWTUtilities;

import fgoScript.util.FileUtils;
import fgoScript.util.GameUtil;
import fgoScript.util.MyLineBorder;
import fgoScript.util.VerifyCodeUtils;
public class Login1 {
	private JFrame frame;//窗体
    private JTextField userNameField;//用户名输入框
    private JPasswordField passwordField;//密码输入框
    private JTextField verifyCodeField;//验证码输入框
    private String verifyCode;//验证码图片中的验证码值
 
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login1 window = new Login1();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * Create the application.
     */
    public Login1() {
        initialize();
    }
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        //将存储验证码文件夹下的所有验证码图片删除
        FileUtils.deleteAllFiles("./verifyCodeImg");
        //自定义圆角输入框边界线
        MyLineBorder myLineBorder = new MyLineBorder(new Color(192, 192, 192), 1 , true);
        //只显示输入框的下边框
        MatteBorder bottomBorder = new MatteBorder(0, 0, 1, 0, new Color(192, 192, 192));
        //设置JFrame禁用本地外观，使用下面自定义设置的外观；
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame();
        frame.setBounds(0, 0, 300, 490);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        /**
         * 对窗体进行基本设置
         */
        //设置窗体在计算机窗口的中心部位显示
        frame.setLocationRelativeTo(frame.getOwner());
        // 去掉窗口的装饰
        frame.setUndecorated(true);
        //采用指定的窗口装饰风格
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        //设置窗体圆角，最后两个参数分别为圆角的宽度、高度数值，一般这两个数值都是一样的
        AWTUtilities.setWindowShape(frame,
                new RoundRectangle2D.Double(0.0D, 0.0D, frame.getWidth(), frame.getHeight(), 20.0D, 20.0D));
        //设置背景颜色，记住一定要修改frame.getContentPane()的颜色，因为我们看到的都是这个的颜色而并不是frame的颜色
        frame.getContentPane().setBackground(Color.white);
        /**
         * 插入顶部非凡汽车背景图片
         */
        //创建具有分层的JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, -5, 300, 200);
        frame.getContentPane().add(layeredPane);
        // 创建图片对象
        ImageIcon img = GameUtil.getBackGroundPreFix(300, 200);
        //设置图片在窗体中显示的宽度、高度
        img.setImage(img.getImage().getScaledInstance(300, 200,Image.SCALE_DEFAULT));
         
        JPanel panel = new JPanel();
        panel.setBounds(0, -5, 300, 200);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
         
        JLabel lblNewLabel = new JLabel("");
        panel.add(lblNewLabel);
        lblNewLabel.setIcon(img);
        /**
         * 插入窗体关闭的背景图片及功能
         */
        // 创建图片对象
        ImageIcon closeImg = GameUtil.getBackGroundPreFix(31, 31);
        //设置图片在窗体中显示的宽度、高度
        closeImg.setImage(closeImg.getImage().getScaledInstance(31, 31,Image.SCALE_DEFAULT));
         
        JPanel closePanel = new JPanel();
        closePanel.setBounds(269, -5, 31, 31);
        layeredPane.add(closePanel,JLayeredPane.MODAL_LAYER);
         
        JLabel closeLabel = new JLabel("");
        closePanel.add(closeLabel);
        closeLabel.setIcon(closeImg);
        closeLabel.addMouseListener(new MouseAdapter() {
            //鼠标点击关闭图片，实现关闭窗体的功能
            @Override
            public void mouseClicked(MouseEvent e) {
                //dispose(); 
                System.exit(0);//使用dispose();也可以关闭只是不是真正的关闭
            }
            //鼠标进入，换关闭的背景图片
            @Override
            public void mouseEntered(MouseEvent e) {
                // 创建图片对象
                ImageIcon closeImg1 = GameUtil.getBackGroundPreFix(31, 31);
                //设置图片在窗体中显示的宽度、高度
                closeImg1.setImage(closeImg1.getImage().getScaledInstance(31, 31,Image.SCALE_DEFAULT));
                closeLabel.setIcon(closeImg1);
            }
            //鼠标离开，换关闭的背景图片
            @Override
            public void mouseExited(MouseEvent e) {
                // 创建图片对象
                ImageIcon closeImg = GameUtil.getBackGroundPreFix(31, 31);
                //设置图片在窗体中显示的宽度、高度
                closeImg.setImage(closeImg.getImage().getScaledInstance(31, 31,Image.SCALE_DEFAULT));
                closeLabel.setIcon(closeImg);
            }
        });
        /**
         * 插入窗体最小化的背景图片及功能
         */
        // 创建图片对象
        ImageIcon minImg =GameUtil.getBackGroundPreFix(31, 31);
        //设置图片在窗体中显示的宽度、高度
        minImg.setImage(minImg.getImage().getScaledInstance(31, 31,Image.SCALE_DEFAULT));
         
        JPanel minPanel = new JPanel();
        minPanel.setBounds(237, -5, 31, 31);
        layeredPane.add(minPanel,JLayeredPane.MODAL_LAYER);
         
        JLabel minLabel = new JLabel("");
        minLabel.addMouseListener(new MouseAdapter() {
            //鼠标点击最小化图片，实现最小化窗体的功能
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setExtendedState(JFrame.ICONIFIED);//最小化窗体
            }
            //鼠标进入，换最小化的背景图片
            @Override
            public void mouseEntered(MouseEvent e) {
                // 创建图片对象
                ImageIcon minImg1 = GameUtil.getBackGroundPreFix(31, 31);
                //设置图片在窗体中显示的宽度、高度
                minImg1.setImage(minImg1.getImage().getScaledInstance(31, 31,Image.SCALE_DEFAULT));
                minLabel.setIcon(minImg1);
            }
            //鼠标离开，换最小化的背景图片
            @Override
            public void mouseExited(MouseEvent e) {
                // 创建图片对象
                ImageIcon minImg = GameUtil.getBackGroundPreFix(31, 31);
                //设置图片在窗体中显示的宽度、高度
                minImg.setImage(minImg.getImage().getScaledInstance(31, 31,Image.SCALE_DEFAULT));
                minLabel.setIcon(minImg);
            }
        });
        minPanel.add(minLabel);
        minLabel.setIcon(minImg);
        /**
         * 插入用户名输入框前面的图片
         */
        // 创建图片对象
        ImageIcon userNameImg = GameUtil.getBackGroundPreFix(40, 40);
        //设置图片在窗体中显示的宽度、高度
        userNameImg.setImage(userNameImg.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT));
         
        JLabel userNameLabel = new JLabel("");
        userNameLabel.setBounds(0, 220, 40, 40);
        userNameLabel.setIcon(userNameImg);
        //默认获取光标
        userNameLabel.setFocusable(true);
        frame.getContentPane().add(userNameLabel);
        /**
         * 添加圆角的用户名输入框
         */
        userNameField = new JTextField();
        userNameField.setBounds(50, 220, 235, 50);
        userNameField.setBorder(bottomBorder);
        userNameField.setText("  用户名");
        userNameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userNameField.setForeground(Color.GRAY);//默认设置输入框中的文字颜色为灰色
        userNameField.addFocusListener(new FocusAdapter() {
            //获取光标事件
            @Override
            public void focusGained(FocusEvent e) {
                //获取焦点时，输入框中内容是“用户名”，那么去掉输入框中显示的内容；
                if("用户名".equals((userNameField.getText().trim()))){
                    userNameField.setText("");
                    userNameField.setForeground(Color.black);//设置颜色为黑色
                }
            }
            //失去光标事件
            @Override
            public void focusLost(FocusEvent e) {
                //失去焦点时，如果输入框中去掉空格后的字符串为空串则显示用户名
                if("".equals((userNameField.getText().trim()))){
                    userNameField.setText("  用户名");
                    userNameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                    userNameField.setForeground(Color.GRAY);//默认设置输入框中的文字颜色为灰色
                }
            }
        });
        frame.getContentPane().add(userNameField);
        userNameField.setColumns(10);
        /**
         * 插入密码输入框前面的图片
         */
        // 创建图片对象
        ImageIcon passwordImg = GameUtil.getBackGroundPreFix(40, 40);
        //设置图片在窗体中显示的宽度、高度
        passwordImg.setImage(passwordImg.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT));
         
        JLabel passwordLabel = new JLabel("");
        passwordLabel.setBounds(0, 280, 40, 40);
        passwordLabel.setIcon(passwordImg);
        frame.getContentPane().add(passwordLabel);
        /**
         * 添加圆角的密码输入框
         */
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 280, 235, 50);
        passwordField.setBorder(bottomBorder);
        passwordField.setText("  密码");
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setForeground(Color.GRAY);//默认设置输入框中的文字颜色为灰色
        passwordField.setEchoChar((char)0);//显示密码输入框中内容
        passwordField.addFocusListener(new FocusAdapter() {
            //获取光标事件
            @SuppressWarnings("deprecation")
			@Override
            public void focusGained(FocusEvent e) {
                //获取焦点时，输入框中内容是“用户名”，那么去掉输入框中显示的内容；
                if("密码".equals((passwordField.getText().trim()))){
                    passwordField.setText("");
                    passwordField.setEchoChar('*');//显示密码输入框中内容
                    passwordField.setForeground(Color.black);//设置颜色为黑色
                }
            }
            //失去光标事件
            @SuppressWarnings("deprecation")
			@Override
            public void focusLost(FocusEvent e) {
                //失去焦点时，如果输入框中去掉空格后的字符串为空串则显示用户名
                if("".equals((passwordField.getText().trim()))){
                    passwordField.setText("  密码");
                    passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                    passwordField.setForeground(Color.GRAY);//默认设置输入框中的文字颜色为灰色
                    passwordField.setEchoChar((char)0);//显示密码输入框中内容
                }
            }
        });
         
        frame.getContentPane().add(passwordField);
        /**
         * 插入验证码输入框前面的图片
         */
        // 创建图片对象
        ImageIcon verifyCodeImg = GameUtil.getBackGroundPreFix(40, 40);
        //设置图片在窗体中显示的宽度、高度
        verifyCodeImg.setImage(verifyCodeImg.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT));
         
        JLabel verifyCodeLabel = new JLabel("");
        verifyCodeLabel.setBounds(0, 340, 40, 40);
        verifyCodeLabel.setIcon(verifyCodeImg);
        frame.getContentPane().add(verifyCodeLabel);
        /**
         * 添加圆角的验证码输入框
         */
        verifyCodeField = new JTextField();
        verifyCodeField.setBounds(50, 340, 135, 50);
        verifyCodeField.setBorder(bottomBorder);
        verifyCodeField.setText("  验证码");
        verifyCodeField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        verifyCodeField.setForeground(Color.GRAY);//默认设置输入框中的文字颜色为灰色
        verifyCodeField.addFocusListener(new FocusAdapter() {
            //获取光标事件
            @Override
            public void focusGained(FocusEvent e) {
                //获取焦点时，输入框中内容是“用户名”，那么去掉输入框中显示的内容；
                if("验证码".equals((verifyCodeField.getText().trim()))){
                    verifyCodeField.setText("");
                    verifyCodeField.setForeground(Color.black);//设置颜色为黑色
                }
            }
            //失去光标事件
            @Override
            public void focusLost(FocusEvent e) {
                //失去焦点时，如果输入框中去掉空格后的字符串为空串则显示用户名
                if("".equals((verifyCodeField.getText().trim()))){
                    verifyCodeField.setText("  验证码");
                    verifyCodeField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                    verifyCodeField.setForeground(Color.GRAY);//默认设置输入框中的文字颜色为灰色
                }
            }
        });
         
        frame.getContentPane().add(verifyCodeField);
        verifyCodeField.setColumns(10);
        /**
         * 添加验证码图片
         */
        JLabel verifyCodeImgLabel = new JLabel("");
        verifyCodeImgLabel.setBounds(190, 340, 95, 50);
        verifyCodeImgLabel.setBorder(myLineBorder);
        frame.getContentPane().add(verifyCodeImgLabel);
        //生成一张验证码图片
        verifyCode = VerifyCodeUtils.createOneCodeImage();
        //将刚生成的验证码图片显示在窗体中去
        ImageIcon verifyCodeSourceImg = GameUtil.getBackGroundPreFix(95, 50);// 创建图片对象
        //设置图片在窗体中显示的宽度、高度
        verifyCodeSourceImg.setImage(verifyCodeSourceImg.getImage().getScaledInstance(95, 50,Image.SCALE_DEFAULT));
        verifyCodeImgLabel.setIcon(verifyCodeSourceImg);
        //点击验证码图片，换一个新的验证码图片
        verifyCodeImgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //删除上一次的验证码图片
                File file = new File("./src/verifyCodeImg/"+verifyCode+".jpg");
                if(file.exists()){
                    file.delete();
                }
                //生成一张新的验证码图片
                verifyCode = VerifyCodeUtils.createOneCodeImage();
                ImageIcon verifyCodeSourceImg1 =GameUtil.getBackGroundPreFix(95, 50);
                verifyCodeSourceImg1.setImage(verifyCodeSourceImg1.getImage().getScaledInstance(95, 50,Image.SCALE_DEFAULT));
                verifyCodeImgLabel.setIcon(verifyCodeSourceImg1);
            }
        });
        /**
         * 添加提示性信息的JLabel
         */
        JLabel reminderMessage = new JLabel("",JLabel.CENTER);
        reminderMessage.setBounds(15, 395, 270, 20);
        reminderMessage.setForeground(Color.red);
        reminderMessage.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        frame.getContentPane().add(reminderMessage);
        /**
         * 添加圆角的提交按钮
         */
        ZButton myButton = new ZButton("安全登录", 0) {
			private static final long serialVersionUID = 1576473128142508589L;
			@Override
			public void runMethod() {
			}
		};
        myButton.setBounds(15, 420, 270, 50);
        frame.getContentPane().add(myButton);
         
         
        //设置窗体可见
        frame.setVisible(true);
    }
}
