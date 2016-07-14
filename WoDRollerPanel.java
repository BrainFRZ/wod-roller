package wodroller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

class WoDRollerPanel extends JPanel {
    public static final String UNSKILLED_STR = "Unskilled";

    private static final Font INPUT_FONT  = new Font("Arial", Font.PLAIN, 12);
    private static final Font OUTPUT_FONT = new Font("Monospaced", Font.PLAIN, 12);

    private static final Integer DEFAULT_NUM_DICE   = WoDRoller.DEFAULT_NUM_DICE;
    private static final Integer DEFAULT_TARGET     = WoDRoller.DEFAULT_TARGET;
    private static final String DEFAULT_ROLL_AGAIN = WoDRoller.DEFAULT_ROLL_AGAIN.toString();

    private final JTextField diceNumField;
    private final JComboBox<Integer> targetDropdown;
    private final JComboBox<String> rollAgainDropdown;
    private final JCheckBox rote, chanceRoll;
    private final JTextArea outputArea;
    private final JButton rollButton, clearButton;

    protected WoDRollerPanel() {
        Integer[] targets = { 10, 9, 8, 7, 6 };


        setLayout(new BorderLayout());

        Box topPanel = Box.createVerticalBox();

        //Set up input panel
        Box inputBox = Box.createHorizontalBox();

        final Dimension BOX_DIMENSION = new Dimension(0, 30);

        JLabel diceNumLabel = new JLabel("Dice:");
        diceNumField = new JTextField(5);
        diceNumField.setMaximumSize(BOX_DIMENSION);
        diceNumField.setFont(INPUT_FONT);

        JLabel targetLabel = new JLabel("Target:");
        targetDropdown = new JComboBox<>(targets);
        targetDropdown.setSelectedItem(DEFAULT_TARGET);

        JLabel rollAgainLabel = new JLabel("Roll again:");
        String[] options = { "Unskilled", "10", "9", "8", "7", "6" };
        rollAgainDropdown = new JComboBox<>(options);
        rollAgainDropdown.setSelectedItem(DEFAULT_ROLL_AGAIN);


        //Create vertical box for two checkboxes
        Box checkboxesArea = Box.createVerticalBox();

        chanceRoll = new JCheckBox("Chance Roll");
        chanceRoll.addActionListener(new CheckboxListener());
        rote       = new JCheckBox("Rote");
        rote.addActionListener(new CheckboxListener());

        checkboxesArea.add(chanceRoll);
        checkboxesArea.add(rote);


        inputBox.add(Box.createHorizontalGlue());
        inputBox.add(diceNumLabel);
        inputBox.add(Box.createHorizontalStrut(5));
        inputBox.add(diceNumField);
        inputBox.add(Box.createHorizontalGlue());
        inputBox.add(targetLabel);
        inputBox.add(Box.createHorizontalStrut(5));
        inputBox.add(targetDropdown);
        inputBox.add(Box.createHorizontalGlue());
        inputBox.add(rollAgainLabel);
        inputBox.add(Box.createHorizontalStrut(5));
        inputBox.add(rollAgainDropdown);
        inputBox.add(Box.createHorizontalGlue());
        inputBox.add(checkboxesArea);
        inputBox.add(Box.createHorizontalGlue());

        topPanel.add(inputBox);

        add(topPanel, BorderLayout.NORTH);

        //Create output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.PAGE_AXIS));

        outputArea = new JTextArea();
        outputArea.setFont(OUTPUT_FONT);
        outputArea.setColumns(75);
        outputArea.setRows(30);
        outputArea.setEditable(false);
        outputArea.setMinimumSize(new Dimension(250, 400));

        JScrollPane outputPane = new JScrollPane(outputArea);
        outputPane.setMinimumSize(new Dimension(250, 400));
        outputPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel resultsLabel = new JLabel("Roll results");
        resultsLabel.setAlignmentX(CENTER_ALIGNMENT);

        outputPanel.setMinimumSize(new Dimension(250, 600));
        outputPanel.add(Box.createHorizontalStrut(10));
        outputPanel.add(resultsLabel);
        outputPanel.add(outputPane);
        outputPanel.add(Box.createHorizontalStrut(10));

        add(outputPanel, BorderLayout.CENTER);

        //Set up action button panel
        Box actionBox = Box.createHorizontalBox();
        rollButton  = new JButton("Roll");
        clearButton = new JButton("Clear");

        ActionListener buttonListener = new ButtonListener();
        rollButton.addActionListener(buttonListener);
        clearButton.addActionListener(buttonListener);

        actionBox.add(Box.createHorizontalGlue());
        actionBox.add(rollButton);
        actionBox.add(Box.createHorizontalStrut(10));
        actionBox.add(clearButton);
        actionBox.add(Box.createHorizontalGlue());

        add(actionBox, BorderLayout.SOUTH);
    }

    protected JButton getDefaultButton() {
        return rollButton;
    }



    public WoDRollEntry parseRollEntry(String entNum, String entTarget, String entRollAgain) {
        final int num, rollAgain, target;

        try {
            num = Integer.parseInt(entNum);
            if (num <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sorry, " + entNum
                    + " is not a valid number of dice!");
        }

        try {
            if (entRollAgain.equalsIgnoreCase(WoDRollerPanel.UNSKILLED_STR)) {
                rollAgain = WoDRoller.DICE_VALUE + 1;
            } else {
                rollAgain = Integer.parseInt(entRollAgain);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("**ERROR**: Roll-again can't be " + entRollAgain);
        }

        try {
            target = Integer.parseInt(entTarget);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("**ERROR**: Illegal target: " + entTarget);
        }

        return (new WoDRollEntry(num, rollAgain, target, rote.isSelected(), chanceRoll.isSelected()));
    }

    private void handleRoll() {
        StringBuilder appendText = new StringBuilder();

        if (diceNumField.getText().isEmpty()) {
            diceNumField.setText(DEFAULT_NUM_DICE.toString());
        }

        WoDRollEntry entry = parseRollEntry(diceNumField.getText(),
                                                targetDropdown.getSelectedItem().toString(),
                                                rollAgainDropdown.getSelectedItem().toString());
        WoDRoll roll = WoDRoller.roll(entry);

        appendText.append(roll.successes).append(" success");
        if (roll.successes == 1) {
            appendText.append(":   ");
        } else {
            appendText.append("es: ");
        }
        appendText.append(roll.rollList.get(0));
        for (int i = 1; i < roll.rollList.size(); i++) {
            appendText.append(",").append(roll.rollList.get(i));
        }

        String wrappedText = wordWrap(appendText.toString(), 87, ",") + "\n\n";

        outputArea.append(wrappedText);

        if (roll.dramaticFailure) {
            JOptionPane.showMessageDialog(this, "Dramatic failure! :(", "Oh no!",
                                            JOptionPane.PLAIN_MESSAGE);
        } else if (roll.exceptionalSuccess) {
            JOptionPane.showMessageDialog(this, "EXCEPTIONAL SUCCESS!! :)", "Huzzah!",
                                            JOptionPane.PLAIN_MESSAGE);
        }
    }

    private static String wordWrap(String text, int width, String delim) {
        StringBuilder out = new StringBuilder();
        String[] words;
        int currentWidth = 0;

        //Parse out tabs and new lines
        text = text.replaceAll("[\t\n]", delim);
        words = text.split(delim);

        //Rewrap to new width
        for (String word : words) {
            if (word.length() >= width) {
                //If it's not the first word, put it on a new line
                if (out.length() == 0) {
                    out.append(delim).append('\n');
                }
                out.append(word).append(delim);
                currentWidth = word.length();
            } else if ((currentWidth + word.length()) <= width) {
                out.append(word).append(delim);
                currentWidth += word.length() + 1;
            } else {
                out.delete(out.length() - 1, out.length());
                out.append(delim).append('\n').append(word).append(delim);
                currentWidth = word.length() + 1;
            }
        }

        return out.substring(0, out.length() - 1);
    }


    private void clearOutput() {
        diceNumField.setText("");
        targetDropdown.setSelectedItem(DEFAULT_TARGET);
        rollAgainDropdown.setSelectedItem(DEFAULT_ROLL_AGAIN);
        chanceRoll.setSelected(false);
        rote.setSelected(false);

        outputArea.setText("");
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == rollButton) {
                if (chanceRoll.isSelected()) {
                    diceNumField.setText("1");
                }
                handleRoll();
            } else if (source == clearButton) {
                clearOutput();
            } else {
                throw new UnsupportedOperationException("Unsupported button was pressed");
            }
        }
    }

    private class CheckboxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == chanceRoll && rote.isSelected()) {
                rote.setSelected(false);
            } else if (source == rote && chanceRoll.isSelected()) {
                chanceRoll.setSelected(false);
            }
        }
    }
}
