package userinterface;

import javafx.scene.control.TextField;

public class NumberOnlyTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.equals("\b")) {
            super.replaceText(start, end, text);
        }
        if (text.matches("[0-9]*") || text.equals(",") || text.equals(".")) {
            if (text.matches("[0-9]*")) {
                super.replaceText(start, end, text);
                return;
            }
            if (!super.getText().contains(".")) {
                if (text.equals(",")) {
                    text = ".";
                }
                super.replaceText(start, end, text);
            }
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (text.equals("\b")) {
            super.replaceSelection(text);
        }
        if (text.matches("[0-9]*") || text.equals(",") || text.equals(".")) {
            if (text.matches("[0-9]*")) {
                super.replaceSelection(text);
                return;
            }
            if (!super.getText().contains(".")) {
                if (text.equals(",")) {
                    text = ".";
                }
                super.replaceSelection(text);
            }
        }
    }
}
