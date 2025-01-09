package id.my.hendisantika.vertx_crud_rest_api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {
  private static final String SERVICE_ADDRESS = "crud.service";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    // Body handler for parsing request bodies
    router.route().handler(BodyHandler.create());

    // GET endpoint for reading all items
    router.get("/items").handler(rc -> {
      vertx.eventBus().<JsonArray>request(SERVICE_ADDRESS, new JsonObject(), reply -> {
        if (reply.succeeded()) {
          rc.response().end(reply.result().body().encode());
        } else {
          rc.fail(500);
        }
      });
    });

    // POST endpoint for creating new items
    router.post("/items").handler(rc -> {
      JsonObject item = rc.getBodyAsJson();
      vertx.eventBus().<JsonObject>request(SERVICE_ADDRESS, item, reply -> {
        if (reply.succeeded()) {
          rc.response().end(reply.result().body().encode());
        } else {
          rc.fail(500);
        }
      });
    });
  }
}
