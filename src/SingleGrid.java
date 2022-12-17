import java.awt.*;

public class SingleGrid {
    private int _gridValue;
    public int xCoord;
    public int yCoord;

    public SingleGrid() {
        _gridValue = 0;
    }

    public void setLoc(int x, int y) {
        xCoord = x;
        yCoord = y;
    }

    public void setGridValue(int value) {
        _gridValue = value;
    }

    public int getGridValue() {
        return _gridValue;
    }

    public Color getBackground() {
        return switch (_gridValue) {
            case 2 -> new Color(0xECE2D9);
            case 4 -> new Color(0xEBDFC7);
            case 8 -> new Color(0xF0B078);
            case 16 -> new Color(0xF39463);
            case 32 -> new Color(0xF47B5F);
            case 64 -> new Color(0xF45E3B);
            case 128 -> new Color(0xEDCF72);
            case 256 -> new Color(0xEDCC61);
            case 512 -> new Color(0xEDC850);
            case 1024 -> new Color(0xEDC53F);
            case 2048 -> new Color(0xEDC22E);
            default -> new Color(0xCCBFB3);
        };
    }

    public Color getFontColor() {
        if (_gridValue == 0)
            return new Color(0xCCBFB3);
        else if (_gridValue < 8)
            return new Color(0x766D65);
        else
            return new Color(0xF7F4F0);
    }

    public Font getFont() {
        if (_gridValue < 100)
            return new Font("Arial", Font.BOLD, 60);
        else if (_gridValue < 1000)
            return new Font("Arial", Font.BOLD, 50);
        else
            return new Font("Arial", Font.BOLD, 40);
    }
}
