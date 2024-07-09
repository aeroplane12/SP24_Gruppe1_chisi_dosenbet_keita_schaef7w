package sw.praktikum.spinfood.model;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw.praktikum.spinfood.Controller;
import sw.praktikum.spinfood.View;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewUserTest {
    private Stage stage;
    private View view;
    private Controller controller;

    @BeforeEach
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            view = new View();
            try {
                stage = new Stage();
                view.start(stage);
                controller = view.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @AfterEach
    public void tearDown() {
        Platform.runLater(() -> stage.close());
    }

    @Test
    public void testUIInteraction() throws InterruptedException {
        Platform.runLater(() -> {
            Button uploadCSVButton = (Button) stage.getScene().lookup("#uploadCSV");
            uploadCSVButton.fire();
            assertEquals(true, uploadCSVButton.isDisabled());
        });
        Thread.sleep(1000);
    }

}
