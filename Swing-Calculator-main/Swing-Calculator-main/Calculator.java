import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {
    private JTextField display;

    public Calculator() {
        setTitle("Java Swing Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 24));
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] buttons = {
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "0",".","=","+"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(this::buttonClicked);
            panel.add(btn);
        }
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void buttonClicked(ActionEvent e) {
        String cmd = ((JButton)e.getSource()).getText();
        if ("=".equals(cmd)) {
            try {
                double result = eval(display.getText());
                display.setText(String.valueOf(result));
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else {
            display.setText(display.getText() + cmd);
        }
    }

    // Simple eval method for basic arithmetic
    private double eval(String expr) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < expr.length()) ? expr.charAt(pos) : -1; }
            boolean eat(int charToEat) { while(ch == ' ') nextChar(); if(ch == charToEat){nextChar(); return true;} return false; }
            double parse() { nextChar(); double x = parseExpression(); if(pos < expr.length()) throw new RuntimeException(); return x; }
            double parseExpression() { double x = parseTerm(); for(;;){ if(eat('+')) x += parseTerm(); else if(eat('-')) x -= parseTerm(); else return x; } }
            double parseTerm() { double x = parseFactor(); for(;;){ if(eat('*')) x *= parseFactor(); else if(eat('/')) x /= parseFactor(); else return x; } }
            double parseFactor() { if(eat('+')) return parseFactor(); if(eat('-')) return -parseFactor(); double x; int startPos = this.pos; if(eat('(')){ x = parseExpression(); eat(')'); } else { while((ch >= '0' && ch <= '9') || ch == '.') nextChar(); x = Double.parseDouble(expr.substring(startPos, this.pos)); } return x; }
        }.parse();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
