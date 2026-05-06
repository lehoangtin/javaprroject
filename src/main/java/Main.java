import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

// Import các giao diện từ thư mục com/parking/gui
import com.parking.gui.LoginDialog;
import com.parking.gui.MainFrame;

/**
 * Lớp khởi chạy chính của hệ thống.
 */
public class Main {
    
    public static void main(String[] args) {
        // 1. Thiết lập giao diện theo hệ điều hành (Windows/macOS)
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) {
            System.err.println("Không thể thiết lập Look and Feel: " + e.getMessage());
        }

        // 2. Khởi chạy luồng giao diện Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Mở hộp thoại đăng nhập trước
                LoginDialog login = new LoginDialog();
                login.setVisible(true);

                // Kiểm tra kết quả đăng nhập
                if (login.isSucceeded()) {
                    // Đăng nhập thành công -> Mở giao diện chính
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                } else {
                    // Tắt hộp thoại mà không đăng nhập -> Thoát app
                    System.out.println("Ứng dụng kết thúc do không đăng nhập.");
                    System.exit(0); 
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khởi động: " + e.getMessage());
            }
        });
    }
}