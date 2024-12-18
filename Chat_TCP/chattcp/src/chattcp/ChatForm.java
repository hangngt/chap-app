package chattcp;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatForm extends JFrame {

    private static JFrame frame;
    private static JPanel contentPane, panel, panel_chat, lbShowMsg;
    private JTextField txtMsg;
    private JList<String> listOnl;
    static DefaultListModel<String> listModel;

    private Socket s = null;
    private static ClientThread myThread;
    static String user;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public ChatForm() {
        try {
            s = new Socket("localhost", 5000);
            myThread = new ClientThread(s);
            myThread.start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    myThread.doSendData("CMD_LOGOUT", frame.getTitle());
                    s.close();
                    System.exit(0);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        setBounds(100, 100, 700, 436);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 0, 685, 395);
        contentPane.add(panel_1);
        panel_1.setLayout(null);

        panel = new JPanel();
        panel.setBounds(0, 0, 644, 234);
        panel.setLayout(null);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(0, 0, 685, 395);
        panel_1.add(panel_2);
        panel_2.setLayout(null);

        JLabel lbOnl = new JLabel("ONLINE");
        lbOnl.setForeground(new Color(0, 128, 0));
        lbOnl.setHorizontalAlignment(SwingConstants.CENTER);
        lbOnl.setFont(new Font("Tahoma", Font.BOLD, 25));
        lbOnl.setBounds(507, 60, 120, 40);
        panel_2.add(lbOnl);

        final JPanel panel_list = new JPanel();
        panel_list.setBackground(new Color(255, 255, 255));
        panel_list.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel_list.setBounds(476, 59, 184, 279);
        panel_2.add(panel_list);
        panel_list.setLayout(null);

        final JLabel lbUsername = new JLabel("Username");
        lbUsername.setFont(new Font("Tahoma", Font.BOLD, 17));
        lbUsername.setBounds(70, 19, 150, 25);
        panel_2.add(lbUsername);

        final JPanel panel_acount = new JPanel();
        panel_acount.setBounds(0, 41, 184, 237);
        panel_list.add(panel_acount);
        panel_acount.setLayout(null);
        listModel = new DefaultListModel<>();

        listOnl = new JList<>(listModel);
        listOnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lbUsername.setText(listOnl.getSelectedValue());
                lbShowMsg.removeAll();
                String receiver = lbUsername.getText();
                String sender = frame.getTitle();
                myThread.doSendData("CMD_OLDMSG", receiver, sender);
            }
        });
        listOnl.setFont(new Font("Tahoma", Font.PLAIN, 17));
        JScrollPane scrollPane = new JScrollPane(listOnl);
        scrollPane.setBounds(0, 0, 184, 237);
        panel_acount.add(scrollPane);

        panel_chat = new JPanel();
        panel_chat.setBackground(new Color(255, 255, 255));
        panel_chat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel_chat.setBounds(26, 59, 440, 320);
        panel_2.add(panel_chat);
        panel_chat.setLayout(null);

        txtMsg = new JTextField();
        txtMsg.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtMsg.setBounds(0, 278, 440, 42);
        panel_chat.add(txtMsg);
        txtMsg.setColumns(10);

        lbShowMsg = new JPanel();
        lbShowMsg.setLayout(new BoxLayout(lbShowMsg, BoxLayout.Y_AXIS));
        lbShowMsg.setBackground(new Color(255, 255, 255));
        JScrollPane scrollPane_1 = new JScrollPane(lbShowMsg);
        scrollPane_1.setBounds(0, 0, 440, 280);
        panel_chat.add(scrollPane_1);

        ImageIcon icon = Icon_load("D:\\my\\hang\\send.png", 46, 40);
        ImageIcon icon_1 = Icon_load("D:\\my\\hang\\3soc.png", 46, 40);
        ImageIcon icon_add = Icon_load("D:\\my\\hang\\add.png", 46, 40);

        JButton btSend = new JButton(icon);
        btSend.setBounds(476, 339, 91, 40);
        panel_2.add(btSend);
        btSend.setForeground(new Color(0, 0, 128));
        btSend.setFont(new Font("Tahoma", Font.BOLD, 20));
        btSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String mess = txtMsg.getText();
                JLabel label = new JLabel(mess);
                label.setOpaque(true);
                label.setBackground(Color.white);
                label.setFont(new Font("Tahoma", Font.PLAIN, 17));
                label.setBorder(new EmptyBorder(5, 5, 10, 5));

                lbShowMsg.add(label);
                lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
                lbShowMsg.revalidate();
                lbShowMsg.repaint();

                txtMsg.setText("");
                user = frame.getTitle();

                Date date = new Date();
                String now = sdf.format(date);
                myThread.doSendData("CMD_SEND", mess, listOnl.getSelectedValue(), user, now);
            }
        });
        btSend.setBackground(new Color(255, 255, 255));
        btSend.setBorder(BorderFactory.createEmptyBorder());

        JButton btOut = new JButton("Log out");
        btOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Do you want to quit?");
                if (confirm == 0) {
                    try {
                        myThread.doSendData("CMD_LOGOUT", frame.getTitle());
                        s.close();
                        System.exit(0);
                        setVisible(false);
                        Login f = new Login();
                        f.setVisible(true);
                        f.setLocationRelativeTo(null);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        btOut.setFont(new Font("Tahoma", Font.BOLD, 17));
        btOut.setBounds(550, 10, 110, 25);
        panel_2.add(btOut);

        JLabel lbTitle = new JLabel("To:");
        lbTitle.setForeground(new Color(0, 128, 0));
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 17));
        lbTitle.setBounds(26, 19, 60, 25);
        panel_2.add(lbTitle);

        JButton btSend_1 = new JButton(icon_1);
        btSend_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String[] options = {"Option 1", "Option 2", "Option 3"};
				int choice = showCustomOptionDialog(frame, "Please choose an option:", options);

				if (choice == 0) {
				    // Người dùng chọn "Option 1"
				} else if (choice == 1) {
				    // Người dùng chọn "Option 2"
				} else if (choice == 2) {
				    // Người dùng chọn "Option 3"
				} else {
				    // Người dùng đã đóng hộp thoại hoặc không chọn gì
				}
            }
        });
        btSend_1.setForeground(new Color(0, 0, 128));
        btSend_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        btSend_1.setBorder(BorderFactory.createEmptyBorder());
        btSend_1.setBackground(Color.WHITE);
        btSend_1.setBounds(569, 339, 91, 40);
        panel_2.add(btSend_1);

        JButton btnCreateGroup = new JButton(icon_add);
        btnCreateGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showListOnlineCreateGroup(null);
                
            }
        });
        btnCreateGroup.setFont(new Font("Tahoma", Font.BOLD, 17));
        btnCreateGroup.setBounds(389, 10, 141, 25);
        panel_2.add(btnCreateGroup);
    }

    public static void getUsername(String username) {
        frame = new ChatForm();
        frame.setTitle(username);
        frame.setVisible(true);
        myThread.doSendData("CMD_ONLINE", username);
    }

    public static void getOnline(String online) {
        listModel.addElement(online);
    }

    public static void getMsg(String sender, String msg) {
        JLabel label1 = new JLabel(sender + ":  " + msg);
        label1.setOpaque(true);
        label1.setForeground(new Color(0, 0, 205));
        label1.setFont(new Font("Tahoma", Font.BOLD, 17));
        label1.setBorder(new EmptyBorder(5, 5, 5, 10));
        lbShowMsg.add(label1);
        lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
        lbShowMsg.revalidate();
        lbShowMsg.repaint();
    }

    public static void getRemove(String name) {
        listModel.removeElement(name);
    }

    public static void getOldMsg(String sender, String message) {
        String userSend = frame.getTitle();
        JLabel items = new JLabel(sender.equals(userSend) ? message : sender + ":  " + message);
        items.setOpaque(true);
        items.setForeground(sender.equals(userSend) ? Color.BLACK : new Color(0, 0, 205));
        items.setFont(new Font("Tahoma", Font.BOLD, 17));
        items.setBorder(new EmptyBorder(5, 5, 5, 10));
        lbShowMsg.add(items);
        lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
        lbShowMsg.revalidate();
        lbShowMsg.repaint();
    }

    private static final long serialVersionUID = 1L;

    public ImageIcon Icon_load(String linkImage, int k, int m) {
        try {
            BufferedImage image = ImageIO.read(new File(linkImage));
            int x = k;
            int y = m;
            int ix = image.getWidth();
            int iy = image.getHeight();
            int dx = 0, dy = 0;

            if ((double)x / y > (double)ix / iy) {
                dy = y;
                dx = dy * ix / iy;
            } else {
                dx = x;
                dy = dx * iy / ix;
            }

            return new ImageIcon(image.getScaledInstance(dx, dy, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int showCustomOptionDialog(JFrame parent, String question, String[] options) {
        return JOptionPane.showOptionDialog(
            parent,
            question,
            "Choose an option",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0] // Giá trị mặc định
        );
    }
    public static JList<CheckListItem> selectedItemsList; // JList để hiển thị các mục đã chọn

    private static void showListOnlineCreateGroup(JFrame parent) {
        JFrame jFrame = createMainFrame();
        Container contentPane = jFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Tạo một DefaultListModel cho danh sách các mục (CheckListItem) trong group
        DefaultListModel<CheckListItem> groupListModel = new DefaultListModel<>();
        for (int i = 0; i < listModel.getSize(); i++) {
            groupListModel.addElement(new CheckListItem(listModel.get(i))); // Thêm CheckListItem
        }

        JList<CheckListItem> groupList = new JList<>(groupListModel);
        groupList.setCellRenderer(new CheckboxListCellRenderer());
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        groupList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int index = groupList.locationToIndex(event.getPoint());
                if (index >= 0) {
                    CheckListItem item = groupList.getModel().getElementAt(index);
                    item.setSelected(!item.isSelected()); // Toggle selected state
                    groupList.repaint(groupList.getCellBounds(index, index)); // Repaint cell
                }
            }
        });

        jFrame.getContentPane().add(new JScrollPane(groupList));

        // Mô hình để hiển thị danh sách các mục đã chọn
        DefaultListModel<CheckListItem> selectedItemsModel = new DefaultListModel<>();
        selectedItemsList = new JList<>(selectedItemsModel); // JList để hiển thị mục đã chọn

        // Nút "Create Group"
        JButton checkSelectBtn = new JButton("Create Group");
        checkSelectBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        checkSelectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder selectedElem = new StringBuilder("You've selected:");
                selectedItemsModel.clear(); // Xóa các mục trước đó

                // Duyệt qua tất cả các mục trong groupListModel và lấy các mục đã chọn
                for (int j = 0; j < groupListModel.getSize(); j++) {
                    CheckListItem item = groupListModel.get(j);
                    if (item.isSelected()) { // Kiểm tra nếu item được chọn
                        selectedElem.append("\n").append(item.getName());
                        selectedItemsModel.addElement(item); // Thêm vào mô hình JList hiển thị mục đã chọn
                    }
                }

                // Sau khi đã chọn các mục, bạn có thể gửi thông tin về group chat tới server
                myThread.setroom(selectedItemsModel.toString());  // Gửi danh sách các mục đã chọn

                JOptionPane.showMessageDialog(jFrame, selectedElem.toString());

                // Thêm tên group chat vào danh sách online (listModel)
                String groupName = selectedItemsModel.toString();  // Tên của group chat
                listModel.addElement(groupName);  // Thêm tên group vào listModel

                // Đảm bảo giao diện được làm mới
                lbShowMsg.revalidate();
                lbShowMsg.repaint();
            }
        });

        contentPane.add(checkSelectBtn, BorderLayout.SOUTH);
        contentPane.add(new JScrollPane(selectedItemsList), BorderLayout.EAST); // Thêm JList hiển thị các mục đã chọn
        jFrame.setSize(600, 400);
        jFrame.setVisible(true);
    }

    
    private static JFrame createMainFrame() {
        JFrame jFrame = new JFrame("Custom ListCellRenderer Example");
        jFrame.setSize(240, 240);
        jFrame.setLocationRelativeTo(null);
//        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return jFrame;
    }

	public void getroom(String msg) {
		
		listModel.addElement(msg);
	}
}