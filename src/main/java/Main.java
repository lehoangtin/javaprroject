import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import com.parking.gui.LoginDialog;
import com.parking.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> 1. Đang khởi động hệ thống..."); 
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println(">>> 2. Đang khởi tạo cửa sổ Đăng nhập...");
                LoginDialog login = new LoginDialog();
                
                System.out.println(">>> 3. Hiển thị LoginDialog (Chờ người dùng nhập liệu)...");
                login.setVisible(true); 

                // Sau khi Dialog đóng, kiểm tra xem đăng nhập có thành công không
                if (login.isSucceeded()) {
                    System.out.println(">>> 4. Đăng nhập THÀNH CÔNG! Đang mở giao diện chính...");
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                } else {
                    System.out.println(">>> 4. Đăng nhập THẤT BẠI hoặc người dùng đã đóng cửa sổ. Thoát ứng dụng.");
                    System.exit(0); 
                }
            } catch (Exception e) {
                // In chi tiết lỗi ra Console để bạn biết chính xác lỗi ở dòng nào
                System.err.println("!!! LỖI KHỞI ĐỘNG NGHIÊM TRỌNG:");
                e.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "Lỗi khởi động: " + e.toString());
            }
        });
    }
}