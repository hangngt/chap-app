package chattcp;

public class CheckListItem {

    private String name; // Tên của mục
    private boolean isSelected; // Trạng thái chọn

    // Constructor
    public CheckListItem(String name) {
        this.name = name;
        this.isSelected = false; // Mặc định chưa chọn
    }

    // Getter cho tên
    public String getName() {
        return name;
    }

    // Kiểm tra xem mục có được chọn không
    public boolean isSelected() {
        return isSelected;
    }

    // Cập nhật trạng thái chọn
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return name; // Để hiển thị tên trong JList
    }
}