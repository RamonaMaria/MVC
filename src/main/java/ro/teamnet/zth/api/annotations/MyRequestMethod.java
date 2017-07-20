package ro.teamnet.zth.api.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Ramona.Raducu on 7/20/2017.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMethod {
    String urlPath();
    String methodType();
}
