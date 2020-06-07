package annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestController
@CrossOrigin(origins="*")
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE
)
public @interface RestCrossOriginController {
  @AliasFor(
      annotation = RequestMapping.class
  )
  String value() default "";
}