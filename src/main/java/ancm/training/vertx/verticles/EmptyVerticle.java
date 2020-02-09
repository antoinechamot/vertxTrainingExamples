package ancm.training.vertx.verticles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;

public class EmptyVerticle extends AbstractVerticle {

  String name;

  private final Logger logger = LoggerFactory.getLogger(EmptyVerticle.class);


  public void start() {
    logger.info("Start");
  }

  public void stop() {
    logger.info("Stop");
  }
}
