package Module;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PointCounter {
    public int count;
    public boolean displayCounter;

    private Label counterLabel;
    private AnchorPane root;

    public PointCounter(AnchorPane root) {
        this.root = root;
        this.count = 0;
        this.displayCounter = false;
        initializeCounter();
    }

    private void initializeCounter() {
        counterLabel = new Label("Points: " + count);
        counterLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        AnchorPane.setTopAnchor(counterLabel, 10.0);
        AnchorPane.setRightAnchor(counterLabel, 10.0);
        root.getChildren().add(counterLabel);
        counterLabel.setVisible(displayCounter);
    }

    public void updateCounter() {
        counterLabel.setText("Points: " + count);
    }

    public void setDisplayCounter(boolean displayCounter) {
        this.displayCounter = displayCounter;
        counterLabel.setVisible(displayCounter);
    }
}

// add points anywhere with
/*
pointCounter.count += 50;
pointCounter.updateCounter();
 */
