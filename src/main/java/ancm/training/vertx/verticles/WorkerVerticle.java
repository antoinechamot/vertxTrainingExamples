package ancm.training.vertx.verticles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;

public class WorkerVerticle extends AbstractVerticle {

  String name;

  private final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);

  public void start() {
    vertx.setPeriodic(10_000, id -> {
      try {
        logger.info("Zzzz....");
        Thread.sleep(8000);
        logger.info("Up!");
      } catch (InterruptedException e) {
        logger.error("Woops", e);
      }
    });
  }

}
