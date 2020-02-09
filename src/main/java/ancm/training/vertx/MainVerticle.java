package ancm.training.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ancm.training.vertx.verticles.EmptyVerticle;
import ancm.training.vertx.verticles.WorkerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

/**
 * Main verticle used to deploy others
 * 
 * @author chamo
 *
 */
public class MainVerticle extends AbstractVerticle {

  String name;
  private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);


  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    // deploy http verticle
    vertx.createHttpServer().requestHandler(req -> {
      req.response().putHeader("content-type", "text/plain").end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        logger.info("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });


    // Deploy several verticles
    long delay = 1000;
    for (int i = 0; i < 50; i++) {
      vertx.setTimer(delay, id -> deploy());
      delay = delay + 1000;
    }

    // Deploy verticle with configuration
    JsonObject conf = new JsonObject().put("n", 2);
    DeploymentOptions opts = new DeploymentOptions().setConfig(conf).setInstances(2);
    vertx.deployVerticle(EmptyVerticle.class, opts);


    // Deploy WorkerVerticle
    DeploymentOptions opts2 = new DeploymentOptions().setInstances(2).setWorker(true);
    vertx.deployVerticle(WorkerVerticle.class, opts);


  }


  private void deploy() {
    vertx.deployVerticle(new EmptyVerticle(), ar -> {
      if (ar.succeeded()) {
        String id = ar.result();
        logger.info("Successfully deployed {}", id);
        vertx.setTimer(5000, tid -> undeployLater(id));
      } else {
        logger.error("Error while deploying", ar.cause());
      }
    });
  }


  private void undeployLater(String id) {
    vertx.undeploy(id, ar -> {
      if (ar.succeeded()) {
        logger.info("{} was undeployed", id);
      } else {
        logger.error("{} could not be undeployed", id);
      }
    });
  }
}
